package com.seancoyle.spacex.framework.presentation.launch

import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@LargeTest
@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchFragmentTests {

  /*  @get:Rule(order = 0)
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
    lateinit var launchDataFactory: LaunchDataFactory

    @Inject
    lateinit var launchNetworkDataSource: LaunchNetworkDataSource

    @Inject
    lateinit var companyInfoNetworkDataSource: CompanyInfoNetworkDataSource

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var dataStore: AppDataStore

    lateinit var validLaunchYears: List<String>
    private lateinit var testLaunchList: List<LaunchModel>
    private lateinit var testCompanyInfoList: CompanyInfoModel

    @Before
    fun init() {
        hiltRule.inject()
        Intents.init()
        prepareDataSet()
        validLaunchYears = launchDataFactory.provideValidFilterYearDates()
    }

    private fun prepareDataSet() = runBlocking {
        // Get fake network data
        testLaunchList = launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        testCompanyInfoList = companyInfoNetworkDataSource.getCompanyInfo()

        // clear any existing data so recyclerview isn't overwhelmed
        launchCacheDataSource.deleteAll()
        companyInfoCacheDataSource.deleteAll()

        // Insert data to fake in memory room database
        launchCacheDataSource.insertList(testLaunchList)
        companyInfoCacheDataSource.insert(testCompanyInfoList)
    }

    @Test
    fun verifyTestDataIsVisible() {

        launchesFragmentTestHelper {
            // Wait for LaunchFragment to come into view
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
    fun filterLaunchItemsByInvalidYear_verifyNoResults() {
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

    }

    @Test
    fun filterByLaunchStatusSuccess_verifyResultsAndDescOrderState() {
        val expectedFilterResults: List<LaunchModel>?

        launchesFragmentTestHelper {
  //
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
            waitViewShown(recyclerViewMatcher)
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
            waitViewShown(recyclerViewMatcher)
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
            waitViewShown(recyclerViewMatcher)
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
            waitViewShown(recyclerViewMatcher)
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

    private fun clearDataStore() = runBlocking {
        dataStore.clearData()
    }

    @After
    fun teardown() {
        clearDataStore()
        Intents.release()
    }*/
}






















