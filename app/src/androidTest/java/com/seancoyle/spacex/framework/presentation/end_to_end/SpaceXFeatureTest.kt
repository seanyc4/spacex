package com.seancoyle.spacex.framework.presentation.end_to_end

import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.seancoyle.spacex.BaseTest
import com.seancoyle.spacex.R
import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoModel
import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.business.interactors.launch.GetAllLaunchItemsFromCache
import com.seancoyle.spacex.di.AppModule
import com.seancoyle.spacex.di.CompanyInfoModule
import com.seancoyle.spacex.di.LaunchModule
import com.seancoyle.spacex.framework.datasource.cache.abstraction.datetransformer.DateTransformer
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_ASC
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_ORDER_DESC
import com.seancoyle.spacex.framework.datasource.data.company.CompanyInfoDataFactory
import com.seancoyle.spacex.framework.datasource.data.launch.LaunchDataFactory
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_EXCEPTION
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_FAILED
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_SUCCESS
import com.seancoyle.spacex.framework.datasource.network.mappers.launch.LAUNCH_UNKNOWN
import com.seancoyle.spacex.framework.presentation.MainActivity
import com.seancoyle.spacex.util.*
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.appTitleViewMatcher
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
import com.seancoyle.spacex.util.LaunchFragmentTestHelper.Companion.recyclerViewMatcher
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import kotlin.random.Random
import kotlin.test.assertTrue

const val HEADER_COUNT = 3

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@LargeTest
@UninstallModules(
    LaunchModule::class,
    CompanyInfoModule::class,
    AppModule::class
)
@RunWith(AndroidJUnit4ClassRunner::class)
class SpaceXFeatureTest : BaseTest() {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val intentsTestRule = ActivityScenarioRule(MainActivity::class.java)

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

    @Inject
    lateinit var dateTransformer: DateTransformer

    @Inject
    lateinit var validLaunchYears: List<String>

    private lateinit var testLaunchList: List<LaunchModel>
    private lateinit var testCompanyInfoList: CompanyInfoModel

    @Before
    fun init() {
        hiltRule.inject()
        testLaunchList = launchDataFactory.produceListOfLaunches()
        testCompanyInfoList = companyInfoDataFactory.produceCompanyInfo()
        prepareDataSet()

    }

    private fun prepareDataSet() = runBlocking {
        // clear any existing data so recyclerview isn't overwhelmed
        launchCacheDataSource.deleteAll()
        launchCacheDataSource.insertLaunchList(testLaunchList)
        companyInfoCacheDataSource.deleteAll()
        companyInfoCacheDataSource.insert(testCompanyInfoList)
    }

    @Test
    fun generalEndToEndTest() {

        /** verifyTestDataIsVisible */
        // Wait for LaunchFragment to come into view
        waitViewShown(recyclerViewMatcher)

        launchesFragmentTestHelper {
            verifyCorrectTextIsDisplayed(
                appTitleViewMatcher,
                text = R.string.app_name
            )
            checkViewIsDisplayed(filterButtonViewMatcher)
            checkViewIsDisplayed(recyclerViewMatcher)
        }

        /** verifyFilterViewIsDisplayingWithCorrectData */
        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyAllFilterDialogsTextViewsDisplayCorrectText()
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterCancelButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
        }

        /** testDaysSinceDisplaysCorrectlyOnLaunchItemsWithAPastDate */

        // Only 2022 launches have "days from now" data
        var year = "2022"
        var expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterLaunchStatusAllViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
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
                expectedFilterResults = expectedFilterResults!!,
                dateTransformer = dateTransformer
            )
        }

        /** filterLaunchItemsByYearDesc_verifyResultsAndDescOrderState */
        year = validLaunchYears.get(index = Random.nextInt(validLaunchYears.size))


        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
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
                expectedFilterResults = expectedFilterResults!!,
                year = year,
            )

            // Check ASC/DESC is still set to DESC after the filter is completed
            // It should remember its state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }

        /** filterLaunchItemsByYearAsc_verifyResultsAndAscOrderState */
        year = validLaunchYears.get(index = Random.nextInt(validLaunchYears.size))

        launchesFragmentTestHelper {
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
                expectedFilterResults = expectedFilterResults!!,
                year = year,
            )

            // Check ASC/DESC is still set to ASC after the filter is completed
            // It should remember its state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }


        /** filterLaunchItemsByInvalidYear_verifyNoResults_toastMessageDisplayedWithError */
        year = "1000"

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
        }

        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                year = year,
                order = LAUNCH_ORDER_DESC
            )
        }

        assertTrue(expectedFilterResults.isNullOrEmpty())

        // Check toast is displayed with error message
        Espresso.onView(ViewMatchers.withText(GetAllLaunchItemsFromCache.GET_ALL_LAUNCH_ITEMS_NO_MATCHING_RESULTS))
            .inRoot(ToastMatcher()).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))


        /** filterByLaunchStatusSuccess_verifyResultsAndDescOrderState */

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsNotChecked(filterAscDescSwitchViewMatcher)
            performClick(filterLaunchStatusSuccessViewMatcher)
            performClick(filterAscDescSwitchViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
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
                expectedFilterResults = expectedFilterResults!!,
                launchSuccessIcon = R.drawable.ic_launch_success,
            )

            // Check Launch Status is still set to SUCCESS after the filter is completed
            // It should remember its state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }


        /** filterByLaunchStatusFailure_verifyResultsAndDescOrderState */

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterLaunchStatusFailureViewMatcher)
            verifyViewIsChecked(filterLaunchStatusFailureViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
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
                expectedFilterResults = expectedFilterResults!!,
                launchSuccessIcon = R.drawable.ic_launch_fail,
            )

            // Check Launch Status is still set to FAILURE after the filter is completed
            // It should remember its state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusFailureViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }


        /** filterByLaunchStatus_invalidSearch */


        runBlocking {
            expectedFilterResults = getFilteredLaunchItemsFromCache(
                isLaunchSuccess = LAUNCH_EXCEPTION,
                order = LAUNCH_ORDER_DESC
            )
        }

        assertTrue(expectedFilterResults.isNullOrEmpty())


        /** filterByLaunchStatusUnknown_verifyResultsAndDescOrderState */

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performClick(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterLaunchStatusUnknownViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
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
                expectedFilterResults = expectedFilterResults!!,
                launchSuccessIcon = R.drawable.ic_launch_unknown,
            )

            // Check Launch Status is still set to UNKNOWN after the filter is completed
            // It should remember its state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterLaunchStatusUnknownViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }


        /** filterLaunchItemsByYear_andLaunchStatusSuccess_verifyResultsAndDescOrderState */
        // We don't use the random list here as some years don't have any successful launches
        // This will cause the test to fail
        year = "2021"

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterLaunchStatusSuccessViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
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
                expectedFilterResults = expectedFilterResults!!,
                year = year,
                launchSuccessIcon = R.drawable.ic_launch_success
            )

            // Check ASC/DESC is still set to DESC after the filter is completed
            // Check Launch Status: SUCCESS is checked
            // These views should remember their state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            performClick(filterCancelButtonViewMatcher)
        }

        /** filterLaunchItemsByYear_andLaunchStatusFailure_verifyResultsAndDescOrderState */
        // We don't use the random list here as some years don't have any successful launches
        // This will cause the test to fail
        year = "2006"

        launchesFragmentTestHelper {
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusAllViewMatcher)
            verifyViewIsChecked(filterLaunchStatusSuccessViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusFailureViewMatcher)
            verifyViewIsNotChecked(filterLaunchStatusUnknownViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            performTypeText(filterYearViewMatcher, text = year)
            performClick(filterLaunchStatusFailureViewMatcher)
            performClick(filterApplyButtonViewMatcher)
            checkViewIsNotDisplayed(filterDialogViewMatcher)
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
                expectedFilterResults = expectedFilterResults!!,
                year = year,
                launchSuccessIcon = R.drawable.ic_launch_fail
            )

            // Check ASC/DESC is still set to DESC after the filter is completed
            // Check Launch Status: SUCCESS is checked
            // These views should remember their state
            performClick(filterButtonViewMatcher)
            checkViewIsDisplayed(filterDialogViewMatcher)
            verifyViewIsChecked(filterAscDescSwitchViewMatcher)
            verifyViewIsChecked(filterLaunchStatusFailureViewMatcher)
            performClick(filterCancelButtonViewMatcher)
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



























