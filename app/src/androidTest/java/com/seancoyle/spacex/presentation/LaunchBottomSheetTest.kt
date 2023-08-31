package com.seancoyle.spacex.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.seancoyle.launch.api.domain.model.LinkType
import com.seancoyle.launch.implementation.presentation.composables.LaunchBottomSheetCard
import com.seancoyle.launch.implementation.presentation.composables.LaunchBottomSheetExitButton
import com.seancoyle.spacex.R
import com.seancoyle.spacex.util.stringResource
import org.junit.Rule
import org.junit.Test

class LaunchBottomSheetTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun launchBottomSheetIsDisplayedCorrectly() {
        val articleString by composeTestRule.stringResource(R.string.article)
        val webCastString by composeTestRule.stringResource(R.string.webcast)
        val wikiString by composeTestRule.stringResource(R.string.wikipedia)
        val linksString by composeTestRule.stringResource(R.string.links)

        composeTestRule.setContent {
            LaunchBottomSheetCard(linkTypes = LINK_TYPES)
            LaunchBottomSheetExitButton {}
        }

        composeTestRule.onNode(hasText(articleString) and hasClickAction()).performClick()
        composeTestRule.onNode(hasText(webCastString) and hasClickAction()).performClick()
        composeTestRule.onNode(hasText(wikiString) and hasClickAction()).performClick()

        composeTestRule.onNodeWithText(articleString).assertIsDisplayed()
        composeTestRule.onNodeWithText(webCastString).assertIsDisplayed()
        composeTestRule.onNodeWithText(wikiString).assertIsDisplayed()
        composeTestRule.onNodeWithText(linksString).assertIsDisplayed()
    }

    companion object {
        private const val DEFAULT_ARTICLE = "https://www.spacex.com"
        private const val DEFAULT_WEBCAST = "https://www.youtube.com"
        private const val DEFAULT_WIKI = "https://www.wikipedia.com"

        private val LINK_TYPES = listOf(
            LinkType(
                link = DEFAULT_ARTICLE,
                nameResId = R.string.article,
                onClick = {}
            ),
            LinkType(
                link = DEFAULT_WEBCAST,
                nameResId = R.string.webcast,
                onClick = {}
            ),
            LinkType(
                link = DEFAULT_WIKI,
                nameResId = R.string.wikipedia,
                onClick = {}
            ),
        )
    }
}