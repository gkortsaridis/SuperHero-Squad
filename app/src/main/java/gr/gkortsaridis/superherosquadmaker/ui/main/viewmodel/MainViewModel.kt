package gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gr.gkortsaridis.superherosquadmaker.data.model.Hero
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    private val heroesDownloaded: MutableList<Hero> = mutableListOf()
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
                    heroesDownloaded.addAll(heroes!!.data.results)
                    _heroes.emit(HerosUiStates.Success(heroesDownloaded, heroes.data.total > (heroes.data.offset + heroes.data.count)))
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

    sealed class HerosUiStates {
        data class Success(val heroes: List<Hero>, val hasMore: Boolean) : HerosUiStates()
        data class Error(val error: String) : HerosUiStates()
        data object Loading : HerosUiStates()
    }
}