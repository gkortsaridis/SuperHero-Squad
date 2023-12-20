package gr.gkortsaridis.superherosquadmaker.ui.hero.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailsViewModel @Inject constructor(
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
}