package gr.gkortsaridis.superherosquadmaker.ui.main.viewmodel

import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase
import gr.gkortsaridis.superherosquadmaker.ui.CoroutineTestRule
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.IRON_MAN_NAME
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.heroesErrorResponse
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.heroesSuccessResponse
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.mockIronMan
import gr.gkortsaridis.superherosquadmaker.ui.hero.viewmodel.HeroDetailsViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var heroViewModel: HeroDetailsViewModel

    private lateinit var mainRepository: MainRepository

    private val marvelApiHelper = mockk<MarvelApiHelper>(relaxed = true)
    private val heroesDatabase = mockk<HeroesDatabase>(relaxed = true)

    @Before
    fun setup() {
        mainRepository = MainRepository(marvelApiHelper, heroesDatabase, coroutinesTestRule.testDispatcherProvider)
    }

    @Test
    fun `test squad flow when no heroes are saved`() = runTest(UnconfinedTestDispatcher()) {
        every { runBlocking { mainRepository.getHeroes() } } returns heroesErrorResponse
        every { runBlocking { mainRepository.getSquad() }  } returns listOf()

        launch {
            viewModel = MainViewModel(mainRepository)
            assert(viewModel.squad.first().isEmpty())
        }
    }

    @Test
    fun `test squad flow when there are heroes saved in Room`() = runTest(UnconfinedTestDispatcher()) {
        every { runBlocking { mainRepository.getHeroes() } } returns heroesErrorResponse
        every { runBlocking { mainRepository.getSquad() }  } returns listOf(mockIronMan, mockIronMan, mockIronMan)

        launch {
            viewModel = MainViewModel(mainRepository)
            val squad = viewModel.squad.first()
            assert(squad.size == 3)
            assert(squad.first().name == IRON_MAN_NAME)
        }
    }

    @Test
    fun `test heroes flow on successful API heroes retrieval`() = runTest(UnconfinedTestDispatcher()) {
        every { runBlocking { mainRepository.getHeroes() } } returns heroesSuccessResponse
        every { runBlocking { mainRepository.getSquad() }  } returns listOf(mockIronMan, mockIronMan, mockIronMan)

        launch {
            viewModel = MainViewModel(mainRepository)
            var heroes = viewModel.heroes.first()
            assert(heroes.javaClass == MainViewModel.HerosUiStates.Loading::class.java)
            heroes = viewModel.heroes.first()
            assert(heroes.javaClass == MainViewModel.HerosUiStates.Success::class.java)
            val heroesSuccess = heroes as MainViewModel.HerosUiStates.Success
            assert(heroesSuccess.heroes.size == 3)
            assert(heroesSuccess.heroes.first().name == IRON_MAN_NAME)
        }
    }

    @Test
    fun `test heroes flow on error API heroes retrieval`() = runTest(UnconfinedTestDispatcher()) {
        every { runBlocking { mainRepository.getHeroes() } } returns heroesErrorResponse
        every { runBlocking { mainRepository.getSquad() }  } returns listOf(mockIronMan, mockIronMan, mockIronMan)

        launch {
            viewModel = MainViewModel(mainRepository)
            var heroes = viewModel.heroes.first()
            assert(heroes.javaClass == MainViewModel.HerosUiStates.Loading::class.java)
            heroes = viewModel.heroes.first()
            assert(heroes.javaClass == MainViewModel.HerosUiStates.Error::class.java)
        }
    }
}