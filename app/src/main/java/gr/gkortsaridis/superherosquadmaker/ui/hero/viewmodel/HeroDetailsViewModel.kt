package gr.gkortsaridis.superherosquadmaker.ui.hero.viewmodel

import android.os.Bundle
import androidx.databinding.ObservableField
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel.MainViewModel

class HeroDetailsViewModel(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _hero = MutableLiveData<Hero>()
    val hero: LiveData<Hero> = _hero

    fun setHeroToDisplay(hero: Hero) {
       _hero.value = hero
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
                    return HeroDetailsViewModel(myRepository) as T
                }
            }
    }
}