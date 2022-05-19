package com.seancoyle.spacex.framework.presentation.launch

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.interactors.launch.GetAllLaunchItemsFromCache
import com.seancoyle.spacex.di.*
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_ASC
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.datasource.data.company.CompanyInfoDataFactory
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_EXCEPTION
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_FAILED
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_SUCCESS
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_UNKNOWN
import com.seancoyle.spacex.framework.presentation.MainActivity
import com.seancoyle.spacex.util.EspressoIdlingResourceRule
import com.seancoyle.spacex.util.LaunchFragmentTestHelper
import com.seancoyle.spacex.util.launchesFragmentTestHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.junit.After
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
@LargeTest
@UninstallModules(
    LaunchModule::class,
    CompanyInfoModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchFragmentTests : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, true)

    @get: Rule(order = 2)
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var launchDataFactory: LaunchDataFactory

    @Inject
    lateinit var companyInfoDataFactory: CompanyInfoDataFactory

    @Inject
    lateinit var launchCacheDataSource: LaunchCacheDataSource

    @Inject
    lateinit var companyInfoCacheDataSource: CompanyInfoCacheDataSource

    private lateinit var testLaunchList: List<LaunchModel>
    private lateinit var testCompanyInfoList: CompanyInfoModel
    private lateinit var validLaunchYears: List<String>

    @Before
    fun init() {
        hiltRule.inject()
        testLaunchList = launchDataFactory.produceListOfLaunches()
        testCompanyInfoList = companyInfoDataFactory.produceCompanyInfo()
        prepareDataSet()

        validLaunchYears = listOf(
            "2022", "2021", "2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013",
            "2012", "2010", "2009", "2008", "2007", "2006"
        ).shuffled()
    }

    private fun prepareDataSet() = runBlocking {
        // clear any existing data so recyclerview isn't overwhelmed
        launchCacheDataSource.deleteAll()
        launchCacheDataSource.insertLaunchList(testLaunchList)
        companyInfoCacheDataSource.deleteAll()
        companyInfoCacheDataSource.insert(testCompanyInfoList)
    }

    @Test
    fun verifyTestDataIsVisible() {
        // Wait for LaunchFragment to come into view
        waitViewShown(LaunchFragmentTestHelper.recyclerViewMatcher)

        launchesFragmentTestHelper {
            verifyCorrectTextIsDisplayed(
                LaunchFragmentTestHelper.appTitleViewMatcher,
                text = R.string.app_name
            )
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.recyclerViewMatcher)
        }
    }

    @Test
    fun verifyFilterViewIsDisplayingWithCorrectData() {
        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyAllFilterDialogsTextViewsDisplayCorrectText()
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performClick(LaunchFragmentTestHelper.filterCancelButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByYearDesc_verifyResultsAndDescOrderState() {
        val year = validLaunchYears.get(index = Random.nextInt(validLaunchYears.size))
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            performTypeText(LaunchFragmentTestHelper.filterYearViewMatcher, text = year)
            performClick(LaunchFragmentTestHelper.filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                year = year,
                order = LAUNCH_ORDER_DESC
            )
        }

        assertTrue(!expectedFilterResults.isNullOrEmpty())

        // Check the date of each item on screen matches the year filter
        launchesFragmentTestHelper {
            checkRecyclerItemsDateMatchesFilteredDate(
                expectedFilterResults = expectedFilterResults,
                year = year,
            )

            // Check ASC/DESC is still set to DESC after the filter is completed
            // It should remember its state
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performClick(LaunchFragmentTestHelper.filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByYearAsc_verifyResultsAndAscOrderState() {
        val year = validLaunchYears.get(index = Random.nextInt(validLaunchYears.size))
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performTypeText(LaunchFragmentTestHelper.filterYearViewMatcher, text = year)
            performClick(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performClick(LaunchFragmentTestHelper.filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                year = year,
                order = LAUNCH_ORDER_ASC
            )
        }

        assertTrue(!expectedFilterResults.isNullOrEmpty())

        // Check the date of each item on screen matches the year filter
        launchesFragmentTestHelper {
            checkRecyclerItemsDateMatchesFilteredDate(
                expectedFilterResults = expectedFilterResults,
                year = year,
            )

            // Check ASC/DESC is still set to ASC after the filter is completed
            // It should remember its state
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performClick(LaunchFragmentTestHelper.filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByInvalidYear_verifyNoResults_toastMessageDisplayedWithError() {
        val year = "1000"
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performTypeText(LaunchFragmentTestHelper.filterYearViewMatcher, text = year)
            performClick(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performClick(LaunchFragmentTestHelper.filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                year = year,
                order = LAUNCH_ORDER_ASC
            )
        }

        assertTrue(expectedFilterResults.isNullOrEmpty())

        // Check toast is displayed with error message
        onView(withText(GetAllLaunchItemsFromCache.GET_ALL_LAUNCH_ITEMS_NO_MATCHING_RESULTS)).inRoot(
            RootMatchers.withDecorView(CoreMatchers.not(activityTestRule.activity.window.decorView))
        )
            .check(matches(isDisplayed()))
    }

    @Test
    fun filterByLaunchStatusSuccess_verifyResultsAndDescOrderState() {
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performClick(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            performClick(LaunchFragmentTestHelper.filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                isLaunchSuccess = LAUNCH_SUCCESS,
                order = LAUNCH_ORDER_DESC
            )
        }

        assertTrue(!expectedFilterResults.isNullOrEmpty())

        // Check the that each view holder has the R.drawable.ic_launch_success icon
        launchesFragmentTestHelper {
            checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatus(
                expectedFilterResults = expectedFilterResults,
                launchSuccessIcon = R.drawable.ic_launch_success,
            )

            // Check Launch Status is still set to SUCCESS after the filter is completed
            // It should remember its state
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            performClick(LaunchFragmentTestHelper.filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterByLaunchStatusFailure_verifyResultsAndDescOrderState() {
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performClick(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            performClick(LaunchFragmentTestHelper.filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                isLaunchSuccess = LAUNCH_FAILED,
                order = LAUNCH_ORDER_DESC
            )
        }

        assertTrue(!expectedFilterResults.isNullOrEmpty())

        // Check the that each view holder has the R.drawable.ic_launch_fail icon
        launchesFragmentTestHelper {
            checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatus(
                expectedFilterResults = expectedFilterResults,
                launchSuccessIcon = R.drawable.ic_launch_fail,
            )

            // Check Launch Status is still set to FAILURE after the filter is completed
            // It should remember its state
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            performClick(LaunchFragmentTestHelper.filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterByLaunchStatus_invalidSearch() {
        val expectedFilterResults: List<LaunchModel>?


        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                isLaunchSuccess = LAUNCH_EXCEPTION,
                order = LAUNCH_ORDER_DESC
            )
        }

        assertTrue(expectedFilterResults.isNullOrEmpty())
    }

    @Test
    fun filterByLaunchStatusUnknown_verifyResultsAndDescOrderState() {
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performClick(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            performClick(LaunchFragmentTestHelper.filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                isLaunchSuccess = LAUNCH_UNKNOWN,
                order = LAUNCH_ORDER_DESC
            )
        }

        assertTrue(!expectedFilterResults.isNullOrEmpty())

        // Check the that each view holder has the R.drawable.ic_launch_unknown icon
        // R.drawable.ic_launch_unknown doesn't display any visible image
        launchesFragmentTestHelper {
            checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatus(
                expectedFilterResults = expectedFilterResults,
                launchSuccessIcon = R.drawable.ic_launch_unknown,
            )

            // Check Launch Status is still set to UNKNOWN after the filter is completed
            // It should remember its state
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            performClick(LaunchFragmentTestHelper.filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByYear_andLaunchStatusSuccess_verifyResultsAndDescOrderState() {
        // We don't use the random list here as some years don't have any successful launches
        // This will cause the test to fail
        val year = "2021"
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performTypeText(LaunchFragmentTestHelper.filterYearViewMatcher, text = year)
            performClick(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            performClick(LaunchFragmentTestHelper.filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                year = year,
                order = LAUNCH_ORDER_DESC,
                isLaunchSuccess = LAUNCH_SUCCESS
            )
        }

        assertTrue(!expectedFilterResults.isNullOrEmpty())

        // Check the date of each item on screen matches the year filter 2021
        launchesFragmentTestHelper {
            checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatusAndYearMatchesFilteredYear(
                expectedFilterResults = expectedFilterResults,
                year = year,
                launchSuccessIcon = R.drawable.ic_launch_success
            )

            // Check ASC/DESC is still set to DESC after the filter is completed
            // Check Launch Status: SUCCESS is checked
            // These views should remember their state
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            performClick(LaunchFragmentTestHelper.filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByYear_andLaunchStatusFailure_verifyResultsAndDescOrderState() {
        // We don't use the random list here as some years don't have any successful launches
        // This will cause the test to fail
        val year = "2006"
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(LaunchFragmentTestHelper.filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            performTypeText(LaunchFragmentTestHelper.filterYearViewMatcher, text = year)
            performClick(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            performClick(LaunchFragmentTestHelper.filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                year = year,
                order = LAUNCH_ORDER_DESC,
                isLaunchSuccess = LAUNCH_FAILED
            )
        }

        assertTrue(!expectedFilterResults.isNullOrEmpty())

        // Check the date of each item on screen matches the year filter 2006
        launchesFragmentTestHelper {
            checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatusAndYearMatchesFilteredYear(
                expectedFilterResults = expectedFilterResults,
                year = year,
                launchSuccessIcon = R.drawable.ic_launch_fail
            )

            // Check ASC/DESC is still set to DESC after the filter is completed
            // Check Launch Status: SUCCESS is checked
            // These views should remember their state
            performClick(LaunchFragmentTestHelper.filterButtonViewMatcher)
            checkViewIsDisplayed(LaunchFragmentTestHelper.filterDialogViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterAscDescSwitchViewMatcher)
            verifyViewIsChecked(LaunchFragmentTestHelper.filterLaunchStatusFailureViewMatcher)
            performClick(LaunchFragmentTestHelper.filterCancelButtonViewMatcher)
        }
    }

    private suspend fun getFilteredLaunchItemsFromCache(
        year: String? = "",
        order: String? = LAUNCH_ORDER_DESC,
        isLaunchSuccess: Int? = null
    ): List<LaunchModel> {
        return launchCacheDataSource.filterLaunchList(
            year = year ?: "",
            order = order ?: LAUNCH_ORDER_DESC,
            isLaunchSuccess = isLaunchSuccess,
            page = 1
        ) ?: emptyList()

    }

    @After
    fun teardown() {
        launchesFragmentTestHelper {
            clearSharedPreferences(ApplicationProvider.getApplicationContext())
        }
    }
}






















