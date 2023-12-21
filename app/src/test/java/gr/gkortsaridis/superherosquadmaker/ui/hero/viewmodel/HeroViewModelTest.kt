package gr.gkortsaridis.superherosquadmaker.ui.hero.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import gr.gkortsaridis.superherosquadmaker.data.api.MarvelApiHelper
import gr.gkortsaridis.superherosquadmaker.data.repository.MainRepository
import gr.gkortsaridis.superherosquadmaker.data.room.HeroesDatabase
import gr.gkortsaridis.superherosquadmaker.ui.CoroutineTestRule
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.mockCaptainAmerica
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.mockHulk
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.mockIronMan
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HeroViewModelTest {

    @get:Rule
    var coroutinesTestRule = InstantTaskExecutorRule()

    private val testDispatcherProvider = CoroutineTestRule().testDispatcherProvider
    private lateinit var viewModel: HeroDetailsViewModel
    private var mainRepository: MainRepository = mockk<MainRepository>(relaxed = true)

    private val marvelApiHelper = mockk<MarvelApiHelper>(relaxed = true)
    private val heroesDatabase = mockk<HeroesDatabase>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcherProvider.main())
        mainRepository = MainRepository(marvelApiHelper, heroesDatabase, testDispatcherProvider)
    }


    @Test
    fun `test that hero will be in squad if saved in Room`() = runTest(UnconfinedTestDispatcher()) {
        every { runBlocking { mainRepository.getSquad() }  } returns listOf(mockIronMan, mockHulk, mockCaptainAmerica)
        viewModel = HeroDetailsViewModel(mainRepository)
        viewModel.setHeroToDisplay(mockIronMan)
        viewModel.heroInSquad.observeForever {  }
        assert(viewModel.heroInSquad.value == true)
    }

    @Test
    fun `test that hero will not be in squad if not saved in Room`() = runTest(UnconfinedTestDispatcher()) {
        every { runBlocking { mainRepository.getSquad() }  } returns listOf(mockHulk, mockCaptainAmerica)
        viewModel = HeroDetailsViewModel(mainRepository)
        viewModel.setHeroToDisplay(mockIronMan)
        viewModel.heroInSquad.observeForever {  }
        assert(viewModel.heroInSquad.value == false)
    }

    /**
     * I am assuming that the Room DB is tested separately
     * So i will not be adding any tests to check for the button presses that add heroes to squad
     */
}