package gr.gkortsaridis.superherosquadmaker.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.superherosquadmaker.usecase.HeroIsInSquadUseCase
import gr.gkortsaridis.superherosquadmaker.usecase.ToggleHeroInSquadUseCase
import javax.inject.Inject

@HiltViewModel
class HeroDetailsViewModel @Inject constructor(
    private val heroIsInSquadUseCase: HeroIsInSquadUseCase,
    private val toggleHeroInSquadUseCase: ToggleHeroInSquadUseCase,
): BaseViewModel() {
    private val _hero = MutableLiveData<Hero>()
    val hero: LiveData<Hero> = _hero

    private val _heroInSquad = MutableLiveData(false)
    val heroInSquad: LiveData<Boolean> = _heroInSquad

    private fun heroIsInSquad() = withUseCaseScope {
        hero.value?.apply {
            _heroInSquad.value = heroIsInSquadUseCase(this)
        }
    }

    fun handleHeroAction() = withUseCaseScope {
        hero.value?.apply {
            toggleHeroInSquadUseCase(this)
            heroIsInSquad()
        }
    }

    fun setHeroToDisplay(hero: Hero) {
       _hero.value = hero
        heroIsInSquad()
    }
}