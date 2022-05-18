package com.seancoyle.spacex.framework.presentation.end_to_end

import androidx.annotation.DrawableRes
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.di.CompanyInfoModule
import com.seancoyle.spacex.di.LaunchModule
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_ASC
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.datasource.data.company.CompanyInfoDataFactory
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_SUCCESS
import com.seancoyle.spacex.framework.presentation.MainActivity
import com.seancoyle.spacex.framework.presentation.RecyclerViewItemCountAssertion
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.appTitleViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.ascDescTitleViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.bottomSheetArticleTitleViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.bottomSheetCancelButtonViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.bottomSheetLinksTitleViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.bottomSheetViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.bottomSheetWikipediaTitleViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.bottomSheetYoutubeTitleViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterApplyButtonViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterAscDescSwitchViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterButtonViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterByYearTitleViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterCancelButtonViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterDialogViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterLaunchStatusAllViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterLaunchStatusFailureViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterLaunchStatusSuccessViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterLaunchStatusUnknownViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.filterYearViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.launchStatusTitleViewMatcher
import com.seancoyle.spacex.framework.presentation.end_to_end.LaunchFragmentTestHelper.Companion.recyclerViewMatcher
import com.seancoyle.spacex.framework.presentation.launch.adapter.LaunchAdapter
import com.seancoyle.spacex.util.EspressoDrawableMatcher.hasDrawable
import com.seancoyle.spacex.util.EspressoIdlingResourceRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
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
class SpaceXFeatureTest : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get: Rule(order = 1)
    val espressoIdlingResourceRule = EspressoIdlingResourceRule()

    @Inject
    lateinit var launchDataFactory: LaunchDataFactory

    @Inject
    lateinit var companyInfoDataFactory: CompanyInfoDataFactory

    @Inject
    lateinit var launchCacheDataSource: LaunchCacheDataSource

    @Inject
    lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @Inject
    lateinit var companyInfoCacheDataSource: CompanyInfoCacheDataSource

    @Inject
    lateinit var companyInfoNetworkDataSource: CompanyInfoNetworkDataSource

    //  private val mockWebServer = MockWebServer()
    private lateinit var testLaunchList: List<LaunchModel>
    private lateinit var testCompanyInfoList: CompanyInfoModel

    @Before
    fun init() {
        hiltRule.inject()
        //   mockWebServer.start()
        testLaunchList = launchDataFactory.produceListOfLaunches()
        testCompanyInfoList = companyInfoDataFactory.produceCompanyInfo()
        prepareDataSet()
    }

    // ** Must clear network and cache so there is no previous state issues **
    private fun prepareDataSet() = runBlocking {
        // clear any existing data so recyclerview isn't overwhelmed
        launchCacheDataSource.deleteAll()
        launchCacheDataSource.insertLaunchList(testLaunchList)
        companyInfoCacheDataSource.deleteAll()
        companyInfoCacheDataSource.insert(testCompanyInfoList)
    }

    @Test
    fun generalEndToEndTest() {

        val scenario = launchActivity<MainActivity>()

        // condition the response
        /*  mockWebServer.enqueue(
              MockResponse()
                  .setResponseCode(HttpURLConnection.HTTP_OK)
                  .setBody(MockWebServerResponseCompanyInfo.companyInfo)
          )*/

        /**
         * Filter test by year - 2020
         * When filter_btn is clicked it should display a filter dialog
         * The user should be able to enter a year value - numeric only, limit 4 digits
         * When they click "apply" the dialog should exit the view and a query to retrieve
         * all launch items with a launch year of 2020 should occur.
         * Test success by scrolling the results and checking the launch date matches 2020
         */

        // Test filter dialog
        var year = "2020"

        // Wait for LaunchFragment to come into view
        waitViewShown(recyclerViewMatcher)

        // confirm LaunchFragment is in view
        launchesFragmentTestHelper {

            verifyCorrectTextIsDisplayed(appTitleViewMatcher, text = R.string.app_name)
            checkViewIsDisplayed(filterButtonViewMatcher)
            checkViewIsDisplayed(recyclerViewMatcher)

            /**
             * Filter test 1: filter by year 2020 with DESC order
             */
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyAllFilterDialogsTextViewsDisplayCorrectText()
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)

            var expectedFilterResults: List<LaunchModel>?
            runBlocking {
                expectedFilterResults = getFilteredLaunchItemsFromCache(
                    year = year,
                    order = LAUNCH_ORDER_DESC
                )
            }

            assertTrue(!expectedFilterResults.isNullOrEmpty())

            // Check the date of each item on screen matches the year filter 2020
            checkRecyclerItemsDateMatchesFilteredDate(
                expectedFilterResults = expectedFilterResults!!,
                year = year,
            )

            /**
             * Filter test 2: filter by year 2021 with ASC order
             */
            year = "2019"

            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterAscDescSwitchViewMatcher)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)

            runBlocking {
                expectedFilterResults = getFilteredLaunchItemsFromCache(
                    year = year,
                    order = LAUNCH_ORDER_ASC
                )
            }

            assertTrue(!expectedFilterResults.isNullOrEmpty())

            // Check the date of each item on screen matches the year filter 2019
            checkRecyclerItemsDateMatchesFilteredDate(
                expectedFilterResults = expectedFilterResults!!,
                year = year,
            )

            // Check ASC/DESC is still set to ASC after the filter is completed
            // It should remember its state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performClick(filterCancelButtonViewMatcher)

            /**
             * Filter test 3: filter by launch status: SUCCESS - default DESC order
             */
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            performClick(filterAscDescSwitchViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)

            runBlocking {
                expectedFilterResults = getFilteredLaunchItemsFromCache(
                    isLaunchSuccess = LAUNCH_SUCCESS,
                    order = LAUNCH_ORDER_DESC
                )
            }

            assertTrue(!expectedFilterResults.isNullOrEmpty())

            // Check the date of each item on screen matches the year filter 2019
            checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatus(
                expectedFilterResults = expectedFilterResults!!,
                launchSuccessIcon = R.drawable.ic_launch_success,
            )

            // Check Launch Status is still set to SUCCESS after the filter is completed
            // It should remember its state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            performClick(filterCancelButtonViewMatcher)









            // Bottom Sheet Tests
            performRecyclerViewClick(recyclerViewMatcher, position = 4)
            checkViewIsDisplayed(bottomSheetViewMatcher)
            verifyAllBottomSheetTextViewsDisplayCorrectTitles()

        }
    }

    private fun checkRecyclerItemsDateMatchesFilteredDate(
        expectedFilterResults: List<LaunchModel>,
        year: String? = ""
    ) {
        launchesFragmentTestHelper {
            onView(recyclerViewMatcher).check(RecyclerViewItemCountAssertion(expectedFilterResults.size))
            for (entity in expectedFilterResults) {
                scrollToRecyclerViewItemWithText(recyclerViewMatcher, entity.launchDate)
                onView(withText(entity.launchDate)).check(matches(withText(containsString(year))))
            }
        }
    }

    private fun checkRecyclerItemsLaunchStatusMatchesFilteredLaunchStatus(
        expectedFilterResults: List<LaunchModel>,
        @DrawableRes launchSuccessIcon: Int
    ) {
        launchesFragmentTestHelper {
            onView(recyclerViewMatcher).check(RecyclerViewItemCountAssertion(expectedFilterResults.size))
            for (entity in expectedFilterResults) {
                scrollToRecyclerViewItemWithText(recyclerViewMatcher, entity.launchDate)
               // onView(allOf(withId(R.id.launch_success_image), isDisplayed().matches(hasDrawable()))
              /*  onView(recyclerViewMatcher)
                    .perform(scrollToPosition(87))
                    .check(matches(atPosition(87, withText("Test Text"))))*/

               /* onView(recyclerViewMatcher).perform(
                    scrollToPosition<LaunchAdapter.LaunchViewHolder>(
                        expectedFilterResults.indexOf(entity)))
                    .check(matches(withId(R.id.launch_success_image)))
                    .check(matches(hasDrawable()))*/

            }
        }
    }

    private fun verifyAllBottomSheetTextViewsDisplayCorrectTitles() {
        launchesFragmentTestHelper {
            verifyCorrectTextIsDisplayed(
                bottomSheetLinksTitleViewMatcher,
                text = R.string.links
            )
            verifyCorrectTextIsDisplayed(
                bottomSheetArticleTitleViewMatcher,
                text = R.string.article
            )
            verifyCorrectTextIsDisplayed(
                bottomSheetYoutubeTitleViewMatcher,
                text = R.string.youtube
            )
            verifyCorrectTextIsDisplayed(
                bottomSheetWikipediaTitleViewMatcher,
                text = R.string.wikipedia
            )
            verifyCorrectTextIsDisplayed(
                bottomSheetCancelButtonViewMatcher,
                text = R.string.text_cancel
            )
        }
    }

    private fun verifyAllFilterDialogsTextViewsDisplayCorrectText() {
        launchesFragmentTestHelper {
            verifyCorrectTextIsDisplayed(
                filterByYearTitleViewMatcher,
                text = R.string.filter_by_year
            )
            verifyCorrectTextIsDisplayed(
                launchStatusTitleViewMatcher,
                text = R.string.launch_status
            )
            verifyCorrectTextIsDisplayed(
                ascDescTitleViewMatcher,
                text = R.string.asc_desc
            )
            verifyCorrectTextIsDisplayed(
                filterCancelButtonViewMatcher,
                text = R.string.text_cancel
            )
            verifyCorrectTextIsDisplayed(
                filterApplyButtonViewMatcher,
                text = R.string.text_apply
            )
            verifyCorrectTextIsDisplayed(
                filterLaunchStatusAllViewMatcher,
                text = R.string.all
            )
            verifyCorrectTextIsDisplayed(
                filterLaunchStatusSuccessViewMatcher,
                text = R.string.success
            )
            verifyCorrectTextIsDisplayed(
                filterLaunchStatusFailureViewMatcher,
                text = R.string.failure
            )
            verifyCorrectTextIsDisplayed(
                filterLaunchStatusUnknownViewMatcher,
                text = R.string.unknown
            )
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
        //   mockWebServer.shutdown()
    }

}



























