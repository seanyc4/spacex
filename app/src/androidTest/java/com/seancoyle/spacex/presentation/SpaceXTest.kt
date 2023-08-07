package com.seancoyle.spacex.presentation

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.espresso.intent.Intents
import androidx.test.filters.LargeTest
import com.seancoyle.core.domain.DateTransformer
import com.seancoyle.core_datastore.AppDataStore
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.spacex.LaunchFactory
import com.seancoyle.spacex.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
@HiltAndroidTest
@LargeTest
class SpaceXTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

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
    lateinit var dateTransformer: DateTransformer

    @Inject
    lateinit var launchFactory: LaunchFactory

    @Inject
    lateinit var launchOptions: LaunchOptions

    @Inject
    lateinit var dataStore: AppDataStore

    lateinit var validLaunchYears: List<String>
    private lateinit var testLaunchList: List<Launch>
    private lateinit var testCompanyInfoList: CompanyInfo

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
        prepareDataSet()
        validLaunchYears = launchFactory.provideValidFilterYearDates()
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

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun verifyLaunchDataIsDisplayed(): Unit = runBlocking {
        val appName = composeTestRule.activity.getString(R.string.app_name)
        val filterBtn = composeTestRule.activity.getString(R.string.filter_btn_content_desc)
        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithText(appName).assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription(filterBtn).assertIsDisplayed()
        composeTestRule.onNodeWithText("HEADER").assertIsDisplayed()
        composeTestRule.onNodeWithText("CAROUSEL").assertIsDisplayed()
        composeTestRule.onNodeWithText("GRID").assertIsDisplayed()


        composeTestRule.onAllNodes(hasTestTag("HEADER")).assertCountEquals(1)
       // composeTestRule.onAllNodes(hasTestTag("SECTION HEADING")).assertCountEquals(4)

       testLaunchList.forEach{ launchItems ->}

    }




    /* @Test
     fun launchBottomSheetLinkOpensExternalBrowser() {
         val expectedIntent = Matchers.allOf(
             IntentMatchers.hasAction(Intent.ACTION_VIEW),
             IntentMatchers.hasData(LaunchBottomSheetTest.DEFAULT_ARTICLE)
         )
         Intents.intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))

         composeTestRule.onNode(
             hasText(articleString)
                     and
                     hasClickAction()
         ).performClick()

         Intents.intended(expectedIntent)
     }*/

}