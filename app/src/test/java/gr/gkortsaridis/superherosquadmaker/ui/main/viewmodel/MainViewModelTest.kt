package gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel

import androidx.lifecycle.viewModelScope
import gr.gkortsaridis.marvelherodownloader.api.MarvelApiHelper
import gr.gkortsaridis.marvelherodownloader.model.Hero
import gr.gkortsaridis.marvelherodownloader.usecase.DownloadHeroesUseCase
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase
import gr.gkortsaridis.superherosquadmaker.ui.CoroutineTestRule
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.IRON_MAN_NAME
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.heroesErrorResponse
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.heroesSuccessResponse
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.mockDataWrapper
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.mockIronMan
import gr.gkortsaridis.superherosquadmaker.usecase.RetrieveSquadUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()
    private val testDispatcherProvider = coroutinesTestRule.testDispatcherProvider
    private lateinit var viewModel: MainViewModel

    private val downloadHeroesUseCase = mockk<DownloadHeroesUseCase>(relaxed = true)
    private val retrieveSquadUseCase = mockk<RetrieveSquadUseCase>(relaxed = true)

    @Test
    fun `test squad flow when no heroes are saved`() = runTest(testDispatcherProvider.unconfined()) {
        coEvery { downloadHeroesUseCase() } returns mockDataWrapper
        coEvery { retrieveSquadUseCase() } returns listOf()

        launch {
            viewModel = MainViewModel(downloadHeroesUseCase, retrieveSquadUseCase)
            assert(viewModel.squad.first().isEmpty())
        }
    }

    @Test
    fun `test squad flow when there are heroes saved in Room`() = runTest(testDispatcherProvider.unconfined()) {
        coEvery { retrieveSquadUseCase() } returns listOf(mockIronMan, mockIronMan, mockIronMan)

        launch {
            viewModel = MainViewModel(downloadHeroesUseCase, retrieveSquadUseCase)
            val squad = viewModel.squad.first()
            assert(squad.size == 3)
            assert(squad.first().name == IRON_MAN_NAME)
        }
    }

    @Test
    fun `test heroes flow on successful API heroes retrieval`() = runTest(testDispatcherProvider.unconfined()) {
        coEvery { downloadHeroesUseCase() } returns mockDataWrapper
        coEvery { retrieveSquadUseCase() } returns listOf()

        launch {
            viewModel = MainViewModel(downloadHeroesUseCase, retrieveSquadUseCase)
            var heroes = viewModel.heroes.first()
            assertEquals(MainViewModel.HeroesUiStates.Loading::class.java, heroes.javaClass)
            heroes = viewModel.heroes.first()
            assertEquals(MainViewModel.HeroesUiStates.Success::class.java, heroes.javaClass)
            val heroesSuccess = heroes as MainViewModel.HeroesUiStates.Success
            assertEquals(heroesSuccess.heroes.size, 3)
            assertEquals(heroesSuccess.heroes.first().name, IRON_MAN_NAME)
        }
    }

    @Test
    fun `test heroes flow on error API heroes retrieval`() = runTest(testDispatcherProvider.unconfined()) {
        val testError = "TEST ERROR"
        coEvery { downloadHeroesUseCase() } throws RuntimeException(testError)
        coEvery { retrieveSquadUseCase()  } returns listOf(mockIronMan, mockIronMan, mockIronMan)

        launch {
            viewModel = MainViewModel(downloadHeroesUseCase, retrieveSquadUseCase)
            var heroes = viewModel.heroes.first()
            assert(MainViewModel.HeroesUiStates.Loading::class.java == heroes.javaClass)
            heroes = viewModel.heroes.first()
            assert(MainViewModel.HeroesUiStates.Error::class.java == heroes.javaClass)
            val heroesError = heroes as MainViewModel.HeroesUiStates.Error
            assert(testError == heroesError.error)
        }
    }
}