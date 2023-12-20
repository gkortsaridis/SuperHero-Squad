package gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import gr.gkortsaridis.superherosquadmaker.data.model.CharacterDataWrapper
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _heroes = MutableSharedFlow<HerosUiStates>()
    val heroes: Flow<HerosUiStates> = _heroes.asSharedFlow()

    private val _squad = MutableSharedFlow<List<Hero>>()
    val squad: Flow<List<Hero>> = _squad.asSharedFlow()

    init {
        getHeroes()
        getSquad()
    }

    fun getHeroes() {
        viewModelScope.launch {
            _heroes.emit(HerosUiStates.Loading)
            try {
                val resp = mainRepository.getHeroes()
                if(resp.isSuccessful) {
                    val heroes = resp.body()
                    Log.i("TEST", "Count"+heroes?.data?.count)
                    Log.i("TEST", "Limit"+heroes?.data?.limit)
                    Log.i("TEST", "Offset"+heroes?.data?.offset)
                    Log.i("TEST", "Total"+heroes?.data?.total)

                    _heroes.emit(HerosUiStates.Success(heroes!!))
                } else {
                    val error = resp.errorBody()
                    _heroes.emit(HerosUiStates.Error(error?.string() ?: "NO ERROR"))
                }
            }catch (ex: Exception) {
                _heroes.emit(HerosUiStates.Error(ex.toString()))
            }
        }
    }

    fun getSquad() {
        viewModelScope.launch {
            val savedSquad = mainRepository.getSquad()
            _squad.emit(savedSquad)
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

    sealed class HerosUiStates {
        data class Success(val heroes: CharacterDataWrapper) : HerosUiStates()
        data class Error(val error: String) : HerosUiStates()
        data object Loading : HerosUiStates()
    }
}