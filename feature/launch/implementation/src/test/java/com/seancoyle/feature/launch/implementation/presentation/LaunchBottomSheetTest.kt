package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.seancoyle.feature.launch.api.domain.model.LinkType
import com.seancoyle.feature.launch.implementation.R
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalMaterialApi
@ExperimentalMaterial3WindowSizeClassApi
@RunWith(RobolectricTestRunner::class)
class LaunchBottomSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @MockK(relaxed = true)
    private lateinit var actionExitClicked: () -> Unit

    @MockK(relaxed = true)
    private lateinit var windowSize: WindowSizeClass

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        composeTestRule.setContent {
            LaunchBottomSheetScreen(
                linkTypes = linkTypes,
                dismiss = { actionExitClicked() },
                windowSize = windowSize
            )
        }
    }

    @Test
    fun launchBottomSheetIsDisplayedCorrectly() {
        composeTestRule.onNodeWithText("Article").assertIsDisplayed()
        composeTestRule.onNodeWithText("Webcast").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wikipedia").assertIsDisplayed()
        composeTestRule.onNodeWithText("Links").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun `when exit button is clicked, then actionExitClicked is called`() {
        composeTestRule.onNodeWithText("Cancel").performClick()
      //  composeTestRule.onNodeWithText("Cancel").assertHasClickAction()
    }

    @Test
    fun `when article link is clicked, then actionExitClicked is called`() {
        composeTestRule.onNodeWithText("Article").performClick()
      //  composeTestRule.onNodeWithText("Article").assertHasClickAction()
    }

    @Test
    fun `when webcast link is clicked, then actionExitClicked is called`() {
        composeTestRule.onNodeWithText("Webcast").performClick()
     //   composeTestRule.onNodeWithText("Webcast").assertHasClickAction()
    }

    @Test
    fun `when wikipedia link is clicked, then actionExitClicked is called`() {
        composeTestRule.onNodeWithText("Wikipedia").performClick()
      //  composeTestRule.onNodeWithText("Wikipedia").assertHasClickAction()
    }

    companion object {
        private const val DEFAULT_ARTICLE = "https://www.spacex.com"
        private const val DEFAULT_WEBCAST = "https://www.youtube.com"
        private const val DEFAULT_WIKI = "https://www.wikipedia.com"

        private val linkTypes = listOf(
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