package com.seancoyle.spacex.presentation

import dagger.hilt.android.testing.HiltAndroidTest

@HiltAndroidTest
open class LaunchBase {

   /* @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var launchCacheDataSource: LaunchCacheDataSource

    @Inject
    lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @Inject
    lateinit var companyCacheDataSource: CompanyCacheDataSource

    @Inject
    lateinit var companyInfoNetworkDataSource: CompanyInfoNetworkDataSource

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var dataStore: AppDataStore

    protected val launchGridTag = "Launch Grid"

    @Before
    @CallSuper
    open fun setup() {
        hiltRule.inject()
        Intents.init()
        getTestDataAndInsertToFakeDatabase()
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
        val testLaunchList = launchNetworkDataSource.getLaunches(launchOptions = launchOptions)
        val testCompanyInfoList = companyInfoNetworkDataSource.getCompany()
        launchCacheDataSource.deleteAll()
        companyCacheDataSource.deleteAll()
        launchCacheDataSource.insertList(testLaunchList)
        companyCacheDataSource.insert(testCompanyInfoList)
    }*/
}