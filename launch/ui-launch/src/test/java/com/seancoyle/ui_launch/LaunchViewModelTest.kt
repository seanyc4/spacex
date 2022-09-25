package com.seancoyle.ui_launch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.seancoyle.core_datastore_test.AppDataStoreManagerFake
import com.seancoyle.launch_datasource_test.CompanyDependencies
import com.seancoyle.launch_datasource_test.LaunchDependencies
import com.seancoyle.launch_models.model.launch.LaunchOptions
import com.seancoyle.launch_usecases.company.CompanyInfoUseCases
import com.seancoyle.launch_usecases.launch.LaunchUseCases
import com.seancoyle.launch_viewstate.LaunchStateEvent
import com.seancoyle.spacex.getOrAwaitValue
import com.seancoyle.ui_launch.ui.LaunchViewModel
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


@ExperimentalCoroutinesApi
@FlowPreview
class LaunchViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private var launchOptions = mockk<LaunchOptions>(relaxed = true)
    private var launchUseCases = mockk<LaunchUseCases>(relaxed = true)
    private var companyInfoUseCases = mockk<CompanyInfoUseCases>(relaxed = true)
    private var dependencyContainer: LaunchDependencies = LaunchDependencies()
    private var companyDependencyContainer: CompanyDependencies = CompanyDependencies()
    private lateinit var dataStore: AppDataStoreManagerFake

    @BeforeEach
    fun setup() {
        dependencyContainer.build()
        companyDependencyContainer.build()
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
    fun getLaunchItemsFromNetwork() = runTest {

        val viewModel = getViewModel()

        advanceUntilIdle()

        viewModel.setStateEvent(
            LaunchStateEvent.GetLaunchItemsFromNetworkAndInsertToCacheEvent(
                launchOptions = launchOptions
            )
        )

        advanceUntilIdle()

        val launchList = dependencyContainer.launchDataFactory.produceListOfLaunchItems()
        assertEquals(viewModel.viewState.getOrAwaitValue()?.launchList, launchList)

    }

}
