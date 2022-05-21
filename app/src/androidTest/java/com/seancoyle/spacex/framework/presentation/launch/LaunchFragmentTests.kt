package com.seancoyle.spacex.framework.presentation.launch

import android.app.Instrumentation
import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.interactors.launch.GetAllLaunchItemsFromCache.Companion.GET_ALL_LAUNCH_ITEMS_NO_MATCHING_RESULTS
import com.seancoyle.spacex.di.*
import com.seancoyle.spacex.framework.datasource.cache.abstraction.datetransformer.DateTransformer
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_ASC
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import com.seancoyle.spacex.framework.datasource.network.abstraction.launch.LaunchRetrofitService
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_EXCEPTION
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_FAILED
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_SUCCESS
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_UNKNOWN
import com.seancoyle.spacex.framework.datasource.network.model.launch.LaunchOptions
import com.seancoyle.spacex.framework.presentation.MainActivity
import com.seancoyle.spacex.util.EspressoIdlingResourceRule
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.appTitleViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.bottomSheetArticleTitleViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.bottomSheetCancelButtonViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.bottomSheetViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.bottomSheetWebcastTitleViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.bottomSheetWikipediaTitleViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterApplyButtonViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterAscDescSwitchViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterButtonViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterCancelButtonViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterDialogViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterLaunchStatusAllViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterLaunchStatusFailureViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterLaunchStatusSuccessViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterLaunchStatusUnknownViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.filterYearViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.materialDialogMessageViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.materialDialogPositiveBtnViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.materialDialogTitleViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.materialDialogViewMatcher
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.recyclerViewMatcher
import com.seancoyle.spacex.util.ToastMatcher
import com.seancoyle.spacex.util.launchesFragmentTestHelper
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
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
    ProductionModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchFragmentTests : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get: Rule(order = 2)
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var launchCacheDataSource: LaunchCacheDataSource

    @Inject
    lateinit var companyInfoCacheDataSource: CompanyInfoCacheDataSource

    @Inject
    lateinit var dateTransformer: DateTransformer

    @Inject
    lateinit var validLaunchYears: List<String>

    @Inject
    lateinit var launchRetrofitService: LaunchRetrofitService

    @Inject
    lateinit var companyInfoRetrofitService: CompanyInfoRetrofitService

    @Inject
    lateinit var launchOptions: LaunchOptions

    private lateinit var testLaunchList: List<LaunchModel>
    private lateinit var testCompanyInfoList: CompanyInfoModel

    @Before
    fun init(){
        hiltRule.inject()
        Intents.init()
        prepareDataSet()
    }

    private fun prepareDataSet() = runBlocking {
        // clear any existing data so recyclerview isn't overwhelmed
        launchCacheDataSource.deleteAll()
        companyInfoCacheDataSource.deleteAll()

        // Get fake network data
        testLaunchList = launchRetrofitService.getLaunchList(launchOptions = launchOptions)
        testCompanyInfoList = companyInfoRetrofitService.getCompanyInfo()

        // Insert data to fake in memory room database
        launchCacheDataSource.insertLaunchList(testLaunchList)
        companyInfoCacheDataSource.insert(testCompanyInfoList)
    }

    @Test
    fun verifyTestDataIsVisible() {
        // Wait for LaunchFragment to come into view
        waitViewShown(recyclerViewMatcher)

        launchesFragmentTestHelper {
            verifyCorrectTextIsDisplayed(
                appTitleViewMatcher,
                text = R.string.app_name
            )
            verifyViewIsDisplayed(filterButtonViewMatcher)
            verifyViewIsDisplayed(recyclerViewMatcher)
        }
    }

    @Test
    fun verifyFilterViewIsDisplayingWithCorrectData() {
        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyAllFilterDialogsTextViewsDisplayCorrectText()
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterCancelButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
        }
    }

    @Test
    fun testDaysSinceDisplaysCorrectlyOnLaunchItemsWithAPastDate() {

        // Only 2022 launches have "days from now" data
        val year = "2022"
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                year = year,
                order = LAUNCH_ORDER_DESC
            )
        }

        assertTrue(!expectedFilterResults.isNullOrEmpty())

        launchesFragmentTestHelper {
            checkRecyclerItemsDaysSinceDisplaysCorrectly(
                expectedFilterResults = expectedFilterResults,
                dateTransformer = dateTransformer
            )
        }
    }

    @Test
    fun filterLaunchItemsByYearDesc_verifyResultsAndDescOrderState() {
        val year = validLaunchYears.get(index = Random.nextInt(validLaunchYears.size))
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
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
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByYearAsc_verifyResultsAndAscOrderState() {
        val year = validLaunchYears.get(index = Random.nextInt(validLaunchYears.size))
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterAscDescSwitchViewMatcher)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
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
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByInvalidYear_verifyNoResults_toastMessageDisplayedWithError() {
        val year = "1000"
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterAscDescSwitchViewMatcher)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                year = year,
                order = LAUNCH_ORDER_ASC
            )
        }

        assertTrue(expectedFilterResults.isNullOrEmpty())

        // Check toast is displayed with error message
        onView(withText(GET_ALL_LAUNCH_ITEMS_NO_MATCHING_RESULTS))
            .inRoot(ToastMatcher()).check(matches(isDisplayed()))

    }

    @Test
    fun filterByLaunchStatusSuccess_verifyResultsAndDescOrderState() {
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
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
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterByLaunchStatusFailure_verifyResultsAndDescOrderState() {
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterLaunchStatusFailureViewMatcher)
            verifyViewIsChecked(filterLaunchStatusFailureViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
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
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusFailureViewMatcher)
            performClick(filterCancelButtonViewMatcher)
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
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterLaunchStatusUnknownViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
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
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusUnknownViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByYear_andLaunchStatusSuccess_verifyResultsAndDescOrderState() {
        // We don't use the random list here as some years don't have any successful launches
        // This will cause the test to fail
        val year = "2021"
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterLaunchStatusSuccessViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
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
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun filterLaunchItemsByYear_andLaunchStatusFailure_verifyResultsAndDescOrderState() {
        // We don't use the random list here as some years don't have any successful launches
        // This will cause the test to fail
        val year = "2006"
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterLaunchStatusFailureViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            verifyViewIsNotDisplayed(filterDialogViewMatcher)
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
            performClick(filterButtonViewMatcher)
            verifyViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            verifyViewIsChecked(filterLaunchStatusFailureViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }
    }

    @Test
    fun recyclerViewOnClickDisplayBottomSheet_isSuccess() {

        // Not all items have links, - no links will display an info dialog
        // so much hard code a position which guarantees links
        val position = 24

        launchesFragmentTestHelper {
            performRecyclerViewClick(recyclerViewMatcher, position)
            verifyViewIsDisplayed(bottomSheetViewMatcher)
            verifyAllBottomSheetTextViewsDisplayCorrectTitles()
        }
    }

    @Test
    fun bottomSheetClickArticleLink_isSuccess() {

        val position = 24
        val articleLink =
            "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/"

        launchesFragmentTestHelper {

            performRecyclerViewClick(recyclerViewMatcher, position)
            verifyViewIsDisplayed(bottomSheetViewMatcher)
            verifyAllBottomSheetTextViewsDisplayCorrectTitles()

            // Build intent to replicate the data equal to the POSITION clicked
            val expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), hasData(articleLink))
            intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))
            performClick(bottomSheetArticleTitleViewMatcher)
            intended(expectedIntent)

        }
    }

    @Test
    fun bottomSheetClickVideoLink_isSuccess() {

        val position = 24
        val videoLink = "https://youtu.be/0giA6VZOICs"

        launchesFragmentTestHelper {

            performRecyclerViewClick(recyclerViewMatcher, position)
            verifyViewIsDisplayed(bottomSheetViewMatcher)
            verifyAllBottomSheetTextViewsDisplayCorrectTitles()

            // Build intent to replicate the data equal to the POSITION clicked
            val expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), hasData(videoLink))
            intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))
            performClick(bottomSheetWebcastTitleViewMatcher)
            intended(expectedIntent)

        }
    }

    @Test
    fun bottomSheetClickWikiLink_isSuccess() {

        val position = 24
        val wikiLink = "https://en.wikipedia.org/wiki/Starlink"

        launchesFragmentTestHelper {

            performRecyclerViewClick(recyclerViewMatcher, position)
            verifyViewIsDisplayed(bottomSheetViewMatcher)
            verifyAllBottomSheetTextViewsDisplayCorrectTitles()

            // Build intent to replicate the data equal to the POSITION clicked
            val expectedIntent = allOf(hasAction(Intent.ACTION_VIEW), hasData(wikiLink))
            intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))
            performClick(bottomSheetWikipediaTitleViewMatcher)
            intended(expectedIntent)

        }
    }

    @Test
    fun bottomSheetClickCancel_isSuccess() {

        val position = 24

        launchesFragmentTestHelper {

            performRecyclerViewClick(recyclerViewMatcher, position)
            verifyViewIsDisplayed(bottomSheetViewMatcher)
            verifyAllBottomSheetTextViewsDisplayCorrectTitles()
            performClick(bottomSheetCancelButtonViewMatcher)
            verifyViewIsNotDisplayed(bottomSheetViewMatcher)

        }
    }

    @Test
    fun bottomSheetNoArticleOrWikiLink_isSuccess() {

        // This item has no article or wiki link in its data
        // Therefore these links should not be visible on screen
        val position = 13

        launchesFragmentTestHelper {
            performRecyclerViewClick(recyclerViewMatcher, position)
            verifyViewIsDisplayed(bottomSheetViewMatcher)
            verifyViewIsNotVisible(bottomSheetArticleTitleViewMatcher)
            verifyViewIsNotVisible(bottomSheetWikipediaTitleViewMatcher)
            verifyViewIsDisplayed(bottomSheetWebcastTitleViewMatcher)
            performClick(bottomSheetCancelButtonViewMatcher)
            verifyViewIsNotDisplayed(bottomSheetViewMatcher)
        }
    }

    @Test
    fun recyclerViewOnClickDisplayBottomSheet_isFail_verifyDialogDisplayWithText() {
        // Select an item with no media links
        // verify a dialog is displayed - also very the title and message
        // Click ok button - check dialog has dismissed
        launchesFragmentTestHelper {
            performRecyclerViewClick(recyclerViewMatcher, 5)
            verifyViewIsNotDisplayed(bottomSheetViewMatcher)
            verifyViewIsDisplayed(materialDialogViewMatcher)
            verifyCorrectTextIsDisplayed(materialDialogTitleViewMatcher, text = R.string.text_info)
            verifyCorrectTextIsDisplayed(materialDialogMessageViewMatcher, text = R.string.no_links)
            performClick(materialDialogPositiveBtnViewMatcher)
            verifyViewIsNotDisplayed(materialDialogViewMatcher)
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
            launchFilter = isLaunchSuccess,
            page = 1
        ) ?: emptyList()

    }

    @After
    fun teardown() {
        launchesFragmentTestHelper {
            clearSharedPreferences(ApplicationProvider.getApplicationContext())
        }
        Intents.release()
    }
}






















