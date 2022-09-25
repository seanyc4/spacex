package com.seancoyle.spacex.framework.presentation.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.business.datastore.AppDataStoreManager
import com.seancoyle.spacex.business.interactors.company.CompanyInfoInteractors
import com.seancoyle.spacex.business.interactors.launch.LaunchInteractors
import com.seancoyle.spacex.di.interactors.launch.LaunchInteractorsModule
import com.seancoyle.spacex.framework.datasource.network.launch.FakeLaunchRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchStateEvent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
@UninstallModules(LaunchInteractorsModule::class)
class LaunchViewModelTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var launchInteractors: LaunchInteractors

   // private val viewModel = mockk<LaunchViewModel>(relaxed = true)
  //  private var launchOptions = mockk<LaunchOptions>(relaxed = true)
  //  private var launchInteractors = mockk<LaunchInteractors>(relaxed = true)
    private var companyInfoInteractors = mockk<CompanyInfoInteractors>(relaxed = true)
    private var dataStore = mockk<AppDataStoreManager>(relaxed = true)

    private lateinit var viewModel : LaunchViewModel

    @Before
    fun init() {
        hiltRule.inject()
        viewModel = LaunchViewModel(
            launchInteractors = launchInteractors,
            companyInfoInteractors = companyInfoInteractors,
            launchOptions = launchOptions,
            appDataStoreManager = dataStore
        )
    }

    @Test
    fun getLaunchItemsFromNetwork(){
        viewModel.setStateEvent(LaunchStateEvent.GetLaunchItemsFromNetworkAndInsertToCacheEvent(
            launchOptions = launchOptions
        ))

        val list = viewModel.getLaunchList()
        assertTrue { !list.isNullOrEmpty() }
    }
}