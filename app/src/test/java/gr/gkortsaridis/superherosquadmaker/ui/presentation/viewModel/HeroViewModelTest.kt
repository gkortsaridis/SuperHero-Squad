package gr.gkortsaridis.superherosquadmaker.ui.presentation.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import gr.gkortsaridis.superherosquadmaker.presentation.viewModel.HeroDetailsViewModel
import gr.gkortsaridis.superherosquadmaker.ui.CoroutineTestRule
import gr.gkortsaridis.superherosquadmaker.ui.DataMocks.mockIronMan
import gr.gkortsaridis.superherosquadmaker.usecase.HeroIsInSquadUseCase
import gr.gkortsaridis.superherosquadmaker.usecase.ToggleHeroInSquadUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
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

    private val heroIsInSquadUseCase = mockk<HeroIsInSquadUseCase>(relaxed = true)
    private val toggleHeroInSquadUseCase = mockk<ToggleHeroInSquadUseCase>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcherProvider.main())
        viewModel = HeroDetailsViewModel(heroIsInSquadUseCase, toggleHeroInSquadUseCase)
    }


    @Test
    fun `test that hero will be in squad if saved in Room`() = runTest(testDispatcherProvider.unconfined()) {
        coEvery { heroIsInSquadUseCase(mockIronMan) } returns true
        viewModel.setHeroToDisplay(mockIronMan)
        viewModel.heroInSquad.observeForever {  }
        assert(viewModel.heroInSquad.value == true)
    }

    @Test
    fun `test that hero will not be in squad if not saved in Room`() = runTest(UnconfinedTestDispatcher()) {
        coEvery { heroIsInSquadUseCase(mockIronMan) } returns false
        viewModel.setHeroToDisplay(mockIronMan)
        viewModel.heroInSquad.observeForever {  }
        assert(viewModel.heroInSquad.value == false)
    }

    /**
     * I am assuming that the Room DB is tested separately
     * So i will not be adding any tests to check for the button presses that add heroes to squad
     */
}