package com.seancoyle.spacex.presentation

import androidx.annotation.CallSuper
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.intent.Intents
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.spacex.LaunchFactory
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
open class LaunchBase {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var launchCacheDataSource: LaunchCacheDataSource

    @Inject
    lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @Inject
    lateinit var companyInfoCacheDataSource: CompanyInfoCacheDataSource

    @Inject
    lateinit var companyInfoNetworkDataSource: CompanyInfoNetworkDataSource

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var launchFactory: LaunchFactory

    @Inject
    lateinit var dataStore: AppDataStore

    protected lateinit var validLaunchYears: List<String>
    protected val launchGridTag = "Launch Grid"

    @Before
    @CallSuper
    open fun setup() {
        hiltRule.inject()
        Intents.init()
        getTestDataAndInsertToFakeDatabase()
        validLaunchYears = launchFactory.provideValidFilterYearDates()
    }

    @After
    @CallSuper
    @Throws(Exception::class)
    open fun tearDown() {
        Intents.release()
        clearDataStore()
    }

    private fun clearDataStore() = runTest {
        dataStore.clearData()
    }

    private fun getTestDataAndInsertToFakeDatabase() = runTest {
        val testLaunchList = launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        val testCompanyInfoList = companyInfoNetworkDataSource.getCompanyInfo()
        launchCacheDataSource.deleteAll()
        companyInfoCacheDataSource.deleteAll()
        launchCacheDataSource.insertList(testLaunchList)
        companyInfoCacheDataSource.insert(testCompanyInfoList)
    }
}