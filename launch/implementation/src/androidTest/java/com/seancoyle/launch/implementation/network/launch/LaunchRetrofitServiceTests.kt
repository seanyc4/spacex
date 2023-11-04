package com.seancoyle.launch.implementation.network.launch

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.launch.implementation.data.network.LaunchNetworkMapper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
@RunWith(AndroidJUnit4ClassRunner::class)
internal class LaunchRetrofitServiceTests {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var networkMapper: LaunchNetworkMapper

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var fakeApi: FakeLaunchApi

    private lateinit var underTest: LaunchNetworkDataSource

    @Before
    fun init() {
        hiltRule.inject()
        underTest = FakeLaunchNetworkDataSourceImpl(
            fakeApi = fakeApi,
            networkMapper = networkMapper
        )
    }

    @Test
    fun getLaunchItems_confirmExpectedResult() = runBlocking {

        val result = underTest.getLaunchList(launchOptions = launchOptions)
        assertTrue(result.isNotEmpty())
    }
}