package com.seancoyle.spacex.framework.presentation.end_to_end

import android.content.Context
import android.content.res.Resources
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.di.CompanyInfoModule
import com.seancoyle.spacex.di.LaunchModule
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_PAGINATION_PAGE_SIZE
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.mappers.company.CompanyInfoCacheMapper
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchCacheMapper
import com.seancoyle.spacex.framework.datasource.cache.model.company.CompanyInfoCacheEntity
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LaunchCacheEntity
import com.seancoyle.spacex.framework.datasource.data.company.CompanyInfoDataFactory
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.presentation.MainActivity
import com.seancoyle.spacex.framework.presentation.RecyclerViewItemCountAssertion
import com.seancoyle.spacex.framework.presentation.launch.adapter.LaunchAdapter.*
import com.seancoyle.spacex.util.EspressoIdlingResourceRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.containsString
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
class SpaceXFeatureTest : BaseTest() {

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
    private fun prepareDataSet() = runBlocking {
        // clear any existing data so recyclerview isn't overwhelmed
        launchDao.deleteAll()
        launchDao.insertList(testLaunchList)
        companyInfoDao.deleteAll()
        companyInfoDao.insert(testCompanyInfoList)
    }

    @Test
    fun generalEndToEndTest() = runBlocking {

        val scenario = launchActivity<MainActivity>()

        // Wait for LaunchFragment to come into view
        waitViewShown(withId(R.id.rv_launch))

        val launchListRecyclerView = onView(withId(R.id.rv_launch))

        // confirm LaunchFragment is in view
        launchListRecyclerView.check(matches(isDisplayed()))


        // Test filter dialog
        val year = "2020"

        // Is modal dialog in view
        onView((withId(R.id.filter_btn))).perform(click())

        // Check dialog is in view
        onView((withId(R.id.filter_dialog_container))).check(matches(isDisplayed()))

        // Enter text into search_query TextInputEditText
        onView(withId(R.id.search_query)).perform(typeText(year))

        // Click apply button
        onView(withId(R.id.apply_btn)).perform(click())

        // Dialog should be gone
        onView(withId(R.id.filter_dialog_container)).check(doesNotExist())

        // Get expected results from the filter - year 2020
        val expectedSearchResults = launchDao.searchLaunchItemsOrderByYearDESC(
            year = year,
            1,
            LAUNCH_PAGINATION_PAGE_SIZE
        )

        // Check the date of each item on screen matches the year filter 2020
        launchListRecyclerView
            .check(RecyclerViewItemCountAssertion(expectedSearchResults.size))
        for (entity in expectedSearchResults) {
            launchListRecyclerView.perform(
                scrollTo<LaunchViewHolder>(hasDescendant(withText(entity.launchDate)))
            )
            onView(withText(entity.launchDate)).check(matches(withText(containsString(year))))
        }

        // Scroll to position 20
        launchListRecyclerView.perform(
            scrollToPosition<LaunchViewHolder>(5)
        )

        // Select an item from the list
        launchListRecyclerView.perform(
            actionOnItemAtPosition<LaunchViewHolder>(5, click())
        )

        // Verify the modal bottom sheet comes into view
        val linksBottomSheet = onView(withId(R.id.links_container)).check(matches(isDisplayed()))

        // Verify all three links - Article / youtube/ Wikipedia are displayed & Title "Links"


    }


}



























