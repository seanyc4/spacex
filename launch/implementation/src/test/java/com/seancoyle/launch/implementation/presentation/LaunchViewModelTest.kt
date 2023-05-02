package com.seancoyle.launch.implementation.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.seancoyle.core.testing.InstantExecutorExtension
import com.seancoyle.core.testing.MainCoroutineRule
import com.seancoyle.core.testing.getOrAwaitValue
import com.seancoyle.core_datastore_test.AppDataStoreManagerFake
import com.seancoyle.launch.api.model.LaunchOptions
import com.seancoyle.launch.implementation.domain.CompanyDependencies
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.LaunchDependencies
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
            mainDispatcher = mainCoroutineRule.testDispatcher,
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

        viewModel.setCompanyInfo(expectedLaunchList)

        val actualLaunchList = viewModel.viewState.getOrAwaitValue(time = 10)?.company
        assertEquals(actualLaunchList, expectedLaunchList)

    }

}