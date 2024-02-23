package com.seancoyle.launch.implementation.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.jupiter.api.extension.ExtendWith


@ExperimentalCoroutinesApi
@FlowPreview
@ExtendWith(com.seancoyle.core_testing.InstantExecutorExtension::class)
class LaunchViewModelTest {

   /* @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var launchOptions: LaunchOptions
    private var launchUseCases = mockk<LaunchUseCases>(relaxed = true)
    private var companyInfoUseCases = mockk<CompanyInfoUseCases>(relaxed = true)
    private var dependencyContainer: LaunchDependencies = LaunchDependencies()
    private var companyDependencyContainer: CompanyDependencies = CompanyDependencies()
    private lateinit var dataStore: AppDataStoreManagerFake
    private lateinit var viewModel: LaunchViewModel

    @BeforeEach
    fun setup() {
        dependencyContainer.build()
        companyDependencyContainer.build()
        launchOptions = dependencyContainer.launchOptions
        dataStore = AppDataStoreManagerFake()
    }

    private fun getViewModel(): LaunchViewModel {
        return LaunchViewModel(
            ioDispatcher = mainCoroutineRule.testDispatcher,
            mainDispatcher = mainCoroutineRule.testDispatcher,
            launchUseCases = launchUseCases,
            companyInfoUseCases = companyInfoUseCases,
            appDataStoreManager = dataStore
        )
    }

    @Test
    fun getCompanyInfoFromNetwork() = runTest {

        viewModel = getViewModel()

//advanceUntilIdle()

        val expectedLaunchList =
            companyDependencyContainer.companyInfoDataFactory.produceCompanyInfo()

        viewModel.setCompanyInfo(expectedLaunchList)

        val actualLaunchList = viewModel.uiState.getOrAwaitValue(time = 10)?.company
        assertEquals(actualLaunchList, expectedLaunchList)

    }
*/
}