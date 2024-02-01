package gr.gkortsaridis.superherosquadmaker.presentation.viewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.marvelherodownloader.usecase.DownloadHeroesUseCase
import gr.gkortsaridis.superherosquadmaker.usecase.RetrieveSquadUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val downloadHeroesUseCase: DownloadHeroesUseCase,
    private val retrieveSquadUseCase: RetrieveSquadUseCase,
) : BaseViewModel() {
    private val heroesDownloaded: MutableList<Hero> = mutableListOf()
    private val _heroes = MutableSharedFlow<HeroesUiStates>()
    val heroes: SharedFlow<HeroesUiStates> = _heroes.asSharedFlow()

    private val _squad = MutableSharedFlow<List<Hero>>()
    val squad: Flow<List<Hero>> = _squad.asSharedFlow()

    init {
        getHeroes()
        getSquad()
    }

    fun getHeroes() = withUseCaseScope (
        loadingUpdater = { isLoading -> _heroes.emit(HeroesUiStates.Loading(isLoading)) },
        onError = { _heroes.emit(HeroesUiStates.Error(it.message ?: "")) }
    ) {
        val heroesResponse = downloadHeroesUseCase()
        heroesDownloaded.addAll(heroesResponse.data.results)
        _heroes.emit(
            HeroesUiStates.Success(
                heroesDownloaded,
                heroesResponse.data.total > (heroesResponse.data.offset + heroesResponse.data.count)
            )
        )
    }

    fun getSquad() = withUseCaseScope(
        loadingUpdater = { },
        onError = { }
    ) {
        val a = retrieveSquadUseCase()
        _squad.emit(a)
    }

    sealed class HeroesUiStates {
        data class Success(val heroes: List<Hero>, val hasMore: Boolean) : HeroesUiStates()
        data class Error(val error: String) : HeroesUiStates()
        data class Loading(val isLoading: Boolean) : HeroesUiStates()
    }
}