package com.seancoyle.spacex.framework.datasource.network.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.di.LaunchModule
import com.seancoyle.spacex.di.ProductionModule
import com.seancoyle.spacex.framework.datasource.api.launch.FakeLaunchApi
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LaunchNetworkMapper
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@UninstallModules(
    LaunchModule::class,
    ProductionModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchRetrofitServiceTests {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // system in test
    private lateinit var apiService: LaunchRetrofitService

    @Inject
    lateinit var networkMapper: LaunchNetworkMapper

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var fakeApi: FakeLaunchApi


    @Before
    fun init() {
        hiltRule.inject()
        apiService = FakeLaunchRetrofitServiceImpl(
            api = fakeApi,
            networkMapper = networkMapper
        )
    }

    @Test
    fun getLaunchItems_confirmExpectedResult() = runBlocking {

        val result = apiService.getLaunchList(launchOptions = launchOptions)
        assertTrue(result.isNotEmpty())

    }

}





































