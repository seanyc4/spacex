package com.seancoyle.spacex.acceptance.launch

import android.app.Instrumentation
import android.content.Intent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.seancoyle.core.test.stringResource
import com.seancoyle.feature.launch.implementation.R
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.FlowPreview
import org.hamcrest.Matchers
import org.junit.Test

@ExperimentalTestApi
@FlowPreview
@HiltAndroidTest
internal class LaunchBottomSheetFragmentTest: LaunchBase() {

    private val launchBottomSheetTag = "Launch Bottom Sheet"
    private val articleString by composeTestRule.stringResource(R.string.article)
    private val wikiString by composeTestRule.stringResource(R.string.wikipedia)

    @Test
    fun verifyLaunchBottomSheetIsDisplayedOnCTA() {
        // Not all items have links, no links will display an info dialog
        // hard code a position which guarantees links
        val position = 9

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
    fun launchBottomSheetArticleLinkCTAOpensExternalBrowser() {
        val articleLink = "https://spaceflightnow.com/2022/03/19/spacex-stretches-rocket-reuse-record-with-another-starlink-launch/"
        launchBottomSheetLinkOpensExternalBrowser(8, articleString, articleLink)
    }

    @Test
    fun launchBottomSheetWebcastLinkCTAOpensExternalBrowser() {
        val webcastString by composeTestRule.stringResource(R.string.webcast)
        val webcastLink = "https://youtu.be/0giA6VZOICs"
        launchBottomSheetLinkOpensExternalBrowser(8, webcastString, webcastLink)
    }

    @Test
    fun launchBottomSheetWikiLinkCTAOpensExternalBrowser() {
        val wikiLink = "https://en.wikipedia.org/wiki/Starlink"
        launchBottomSheetLinkOpensExternalBrowser(8, wikiString, wikiLink)
    }

    @Test
    fun launchBottomSheetCancelCTADismissesView() {
        composeTestRule.apply{
            val cancelString by stringResource(R.string.text_cancel)
            waitUntilAtLeastOneExists(hasTestTag(launchGridTag))

            onNodeWithTag(launchGridTag)
                .performScrollToIndex(30)
                .performClick()

            onNode(
                hasText(cancelString)
                        and
                        hasClickAction()
            )
                .assertIsDisplayed()
                .performClick()

            onNodeWithTag(launchBottomSheetTag).assertDoesNotExist()
        }
    }

    @Test
    fun launchBottomSheetArticleAndWikiLinkNotDisplayed(){
        composeTestRule.apply{
            waitUntilAtLeastOneExists(hasTestTag(launchGridTag))

            onNodeWithTag(launchGridTag)
                .performScrollToIndex(19)
                .performClick()

            onNodeWithText(articleString).assertDoesNotExist()
            onNodeWithText(wikiString).assertDoesNotExist()
        }
    }

    /*@Test
    fun launchBottomSheetNoLinksDisplaysDialogue(){
        composeTestRule.apply {
            waitUntilAtLeastOneExists(hasTestTag(launchGridTag))

            onNodeWithTag(launchGridTag)
                .performScrollToIndex(10)
                .performClick()
        }

        Espresso.onView(withId(R.id.md_text_title)).check(matches(withText(R.string.text_info)))
        Espresso.onView(withId(R.id.md_text_message)).check(matches(withText(R.string.no_links)))
    }
*/
    private fun launchBottomSheetLinkOpensExternalBrowser(
        position: Int,
        linkString: String,
        link: String
    ) {
        val expectedIntent = Matchers.allOf(
            IntentMatchers.hasAction(Intent.ACTION_VIEW),
            IntentMatchers.hasData(link)
        )
        Intents.intending(expectedIntent).respondWith(Instrumentation.ActivityResult(0, null))

        composeTestRule.apply {
            waitUntilAtLeastOneExists(hasTestTag(launchGridTag))

            onNodeWithTag(launchGridTag)
                .performScrollToIndex(position)
                .performClick()

            onNode(
                hasText(linkString)
                        and
                        hasClickAction(),
                useUnmergedTree = true
            )
                .assertIsDisplayed()
                .performClick()
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