package gr.gkortsaridis.superherosquadmaker.ui.hero.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import kotlinx.coroutines.launch

class HeroDetailsViewModel(
    private val mainRepository: MainRepository
): ViewModel() {
    private val _hero = MutableLiveData<Hero>()
    val hero: LiveData<Hero> = _hero

    private val _heroInSquad = MutableLiveData(false)
    val heroInSquad: LiveData<Boolean> = _heroInSquad

    private fun heroIsInSquad() = viewModelScope.launch {
        _heroInSquad.postValue(mainRepository.getSquad().find { it.id == hero.value?.id } != null)
    }

    fun handleHeroAction() = viewModelScope.launch {
        if (heroInSquad.value == true) {
            mainRepository.fireHeroFromSquad(hero.value!!)
        } else {
            mainRepository.addHeroToSquad(hero.value!!)
        }
        heroIsInSquad()
    }

    fun setHeroToDisplay(hero: Hero) {
       _hero.value = hero
        heroIsInSquad()
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