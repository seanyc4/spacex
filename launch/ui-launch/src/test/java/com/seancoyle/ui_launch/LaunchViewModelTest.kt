package com.seancoyle.ui_launch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.seancoyle.core.testing.InstantExecutorExtension
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.core_datastore_test.AppDataStoreManagerFake
import com.seancoyle.launch_datasource_test.CompanyDependencies
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_models.model.launch.LaunchOptions
import com.seancoyle.launch_usecases.company.CompanyInfoUseCases
import com.seancoyle.launch_usecases.launch.LaunchUseCases
import com.seancoyle.core.testing.getOrAwaitValue
import com.seancoyle.ui_launch.ui.LaunchViewModel
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExperimentalCoroutinesApi
@FlowPreview
@ExtendWith(InstantExecutorExtension::class)
class LaunchViewModelTest {

    @get:Rule
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
            launchUseCases = launchUseCases,
            companyInfoUseCases = companyInfoUseCases,
            launchOptions = launchOptions,
            appDataStoreManager = dataStore
        )
    }

    @Test
    fun getCompanyInfoFromNetwork() = runTest {

        viewModel = getViewModel()

//advanceUntilIdle()

        val expectedLaunchList =
            companyDependencyContainer.companyInfoDataFactory.produceCompanyInfo()
        val actualLaunchList = viewModel.viewState.getOrAwaitValue(time = 10)?.company
        assertEquals(actualLaunchList, expectedLaunchList)

    }

}