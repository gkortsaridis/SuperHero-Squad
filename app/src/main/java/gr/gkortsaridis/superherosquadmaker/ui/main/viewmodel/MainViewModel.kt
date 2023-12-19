package gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {
    init {
        getHeroes()
    }

    fun getHeroes() {
        viewModelScope.launch {
            try {
                val resp = mainRepository.getHeroes()
                if(resp.isSuccessful) {
                    val heroes = resp.body()
                    Log.i("TEST", "Received ${heroes?.data?.results?.size} heroes")
                } else {
                    val error = resp.errorBody()
                    Log.i("TEST", error?.string() ?: "NO ERROR")
                }
            } catch (ex: Exception) {
                Log.e("TEST", ex.toString())
            }
        }

    }

    companion object {
        //Ideally we should setup a proper DI solution.
        //For the quick project, creating these factories should be ok
        fun provideFactory(
            myRepository: MainRepository,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return MainViewModel(myRepository) as T
                }
            }
    }

}