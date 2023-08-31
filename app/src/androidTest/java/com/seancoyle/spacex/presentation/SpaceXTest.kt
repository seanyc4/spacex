package com.seancoyle.spacex.presentation

import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.filters.LargeTest
import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.data.LaunchNetworkDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchOptions
import com.seancoyle.spacex.LaunchFactory
import com.seancoyle.spacex.R
import com.seancoyle.spacex.util.stringResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalTestApi
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
    lateinit var launchFactory: LaunchFactory

    @Inject
    lateinit var launchOptions: LaunchOptions

    lateinit var validLaunchYears: List<String>
    private lateinit var testLaunchList: List<Launch>
    private lateinit var testCompanyInfoList: CompanyInfo
    private val launchGridTag = "Launch Grid"
    private val launchBottomSheetTag = "Launch Bottom Sheet"

    @Before
    fun setup() {
        hiltRule.inject()
        Intents.init()
        getTestDataAndInsertToFakeDatabase()
        validLaunchYears = launchFactory.provideValidFilterYearDates()
    }

    private fun getTestDataAndInsertToFakeDatabase() = runTest {
        testLaunchList = launchNetworkDataSource.getLaunchList(launchOptions = launchOptions)
        testCompanyInfoList = companyInfoNetworkDataSource.getCompanyInfo()
        launchCacheDataSource.deleteAll()
        companyInfoCacheDataSource.deleteAll()
        launchCacheDataSource.insertList(testLaunchList)
        companyInfoCacheDataSource.insert(testCompanyInfoList)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun verifyLaunchScreenAndGridIsDisplayed() {
        composeTestRule.apply {
            val appName by stringResource(R.string.app_name)
            val filterBtn by stringResource(R.string.filter_btn_content_desc)

            waitUntilAtLeastOneExists(hasTestTag(launchGridTag))

            onNodeWithText(appName).assertIsDisplayed()
            onNodeWithContentDescription(filterBtn).assertIsDisplayed()
            onNodeWithTag(launchGridTag).assertIsDisplayed()
        }
    }

    @Test
    fun verifyLaunchItemOnClickBottomSheetIsDisplayed() {
        // Not all items have links, no links will display an info dialog
        // hard code a position which guarantees links
        val position = 30

        composeTestRule.apply {
            waitUntilAtLeastOneExists(hasTestTag(launchGridTag))

            onNodeWithTag(launchGridTag)
                .performScrollToIndex(position)
                .performClick()

            onNodeWithTag(launchBottomSheetTag).assertIsDisplayed()
        }

        verifyBottomSheetTextIsDisplayed()
    }

    @Test
    fun launchBottomSheetLinkOpensExternalBrowser() {
        val position = 30
        val articleString by composeTestRule.stringResource(R.string.article)
        val articleLink = "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/"
        val expectedIntent = Matchers.allOf(
            IntentMatchers.hasAction(Intent.ACTION_VIEW),
            IntentMatchers.hasData(articleLink)
        )
        Intents.intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))

        composeTestRule.apply {
            waitUntilAtLeastOneExists(hasTestTag(launchGridTag))

            onNodeWithTag(launchGridTag)
                .performScrollToIndex(position)
                .performClick()

            onNode(
                matcher = hasText(articleString)
                        and
                        hasClickAction()
            ).performClick()
        }

        Intents.intended(expectedIntent)
    }

    private fun verifyBottomSheetTextIsDisplayed() {
        composeTestRule.apply {
            val articleString by stringResource(R.string.article)
            val webCastString by stringResource(R.string.webcast)
            val wikiString by stringResource(R.string.wikipedia)
            val linksString by stringResource(R.string.links)

            onNodeWithText(articleString).assertIsDisplayed()
            onNodeWithText(webCastString).assertIsDisplayed()
            onNodeWithText(wikiString).assertIsDisplayed()
            onNodeWithText(linksString).assertIsDisplayed()
        }
    }

}