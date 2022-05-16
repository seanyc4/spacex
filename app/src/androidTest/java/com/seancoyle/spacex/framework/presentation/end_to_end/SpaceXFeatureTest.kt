package com.seancoyle.spacex.framework.presentation.end_to_end

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.di.CompanyInfoModule
import com.seancoyle.spacex.di.LaunchModule
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoCacheMapper
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchCacheMapper
import com.seancoyle.spacex.framework.datasource.cache.model.company.CompanyInfoCacheEntity
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LaunchCacheEntity
import com.seancoyle.spacex.framework.datasource.data.company.CompanyInfoDataFactory
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.presentation.MainActivity
import com.seancoyle.spacex.framework.presentation.launch.adapter.LaunchAdapter.*
import com.seancoyle.spacex.util.EspressoIdlingResourceRule
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

/*
    --Test cases:
    1. Navigate LaunchFragment, confirm list is visible
    2. Scroll to the end of the list.
    3. Select an item from list

 */

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@LargeTest
@UninstallModules(
    LaunchModule::class,
    CompanyInfoModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class SpaceXFeatureTest: BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var launchCacheMapper: LaunchCacheMapper

    @Inject
    lateinit var companyInfoCacheMapper: CompanyInfoCacheMapper

    @Inject
    lateinit var launchDataFactory: LaunchDataFactory

    @Inject
    lateinit var companyInfoDataFactory: CompanyInfoDataFactory

    @Inject
    lateinit var launchDao: LaunchDao

    @Inject
    lateinit var companyInfoDao: CompanyInfoDao

    @Inject
    lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @Inject
    lateinit var companyInfoNetworkDataSource: CompanyInfoNetworkDataSource

    private lateinit var testLaunchList: List<LaunchCacheEntity>
    private lateinit var testCompanyInfoList: CompanyInfoCacheEntity

    @Before
    fun init() {
        hiltRule.inject()
        testLaunchList = launchCacheMapper.domainListToEntityList(
            launchDataFactory.produceListOfLaunches()
        )
        testCompanyInfoList = companyInfoCacheMapper.mapToEntity(
            companyInfoDataFactory.produceCompanyInfo()
        )
        prepareDataSet()
    }

    // ** Must clear network and cache so there is no previous state issues **
    private fun prepareDataSet() = runBlocking{
        // clear any existing data so recyclerview isn't overwhelmed
        launchDao.deleteAll()
        launchDao.insertList(testLaunchList)
        companyInfoDao.deleteAll()
        companyInfoDao.insert(testCompanyInfoList)
    }

    @Test
    fun generalEndToEndTest(){

        val scenario = launchActivity<MainActivity>()

        // Wait for LaunchFragment to come into view
        waitViewShown(withId(R.id.rv_launch))

        val launcListRecyclerView = onView(withId(R.id.rv_launch))

        // confirm LaunchFragment is in view
        launcListRecyclerView.check(matches(isDisplayed()))

        // Scroll to bottom of the list
        launcListRecyclerView.perform(
            scrollToPosition<LaunchViewHolder>(testLaunchList.size.minus(1))
        )

        // Select an item from the list
        launcListRecyclerView.perform(
            actionOnItemAtPosition<LaunchViewHolder>(1, click())
        )

      /*  // press hardware back button
        Espresso.pressBack()

        // confirm LaunchFragment is in view
        launcListRecyclerView.check(matches(isDisplayed()))*/
    }

}



























