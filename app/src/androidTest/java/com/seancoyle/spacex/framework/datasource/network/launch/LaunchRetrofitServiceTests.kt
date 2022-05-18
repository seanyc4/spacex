package com.seancoyle.spacex.framework.datasource.network.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.di.LaunchModule
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchApi
import com.seancoyle.spacex.framework.datasource.network.implementation.launch.LaunchRetrofitServiceImpl
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
import kotlin.random.Random
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@UninstallModules(
    LaunchModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchRetrofitServiceTests : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    // system in test
    private lateinit var apiService: LaunchRetrofitService

    // dependencies
    @Inject
    lateinit var api: LaunchApi

    @Inject
    lateinit var networkMapper: LaunchNetworkMapper

    @Inject
    lateinit var dataFactory: LaunchDataFactory

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var launchDataFactory: LaunchDataFactory


    @Before
    fun init() {
        hiltRule.inject()
        apiService = LaunchRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }

    @Test
    fun getLaunchItemsFromNetwork_confirmExpectedResult() = runBlocking {

        val expectedResult = launchDataFactory.produceListOfLaunches()

        val result = apiService.getLaunchList(launchOptions = launchOptions)

        assertTrue(result.isNotEmpty())

        assertTrue(actual = result[Random.nextInt(0, result.size)] is LaunchModel)

      //  assertEquals(result, expectedResult)
    }

}





































