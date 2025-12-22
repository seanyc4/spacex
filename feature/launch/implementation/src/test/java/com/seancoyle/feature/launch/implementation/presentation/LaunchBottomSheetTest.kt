package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.seancoyle.feature.launch.implementation.R
import com.seancoyle.feature.launch.implementation.presentation.model.BottomSheetLinksUi
import com.seancoyle.feature.launch.implementation.presentation.state.BottomSheetUiState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalMaterial3Api
@ExperimentalMaterial3WindowSizeClassApi
@RunWith(RobolectricTestRunner::class)
class LaunchBottomSheetTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @MockK(relaxed = true)
    private lateinit var onEvent: (LaunchesEvents) -> Unit

    private lateinit var bottomSheetUiState: BottomSheetUiState

    private var isLandScape = false

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        bottomSheetUiState = BottomSheetUiState(
            isVisible = true,
            bottomSheetLinks = bottomSheetLinks
        )

        composeTestRule.setContent {
            LaunchBottomSheetScreen(
                bottomSheetUiState = bottomSheetUiState,
                onEvent = onEvent,
                isLandscape = isLandScape
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
        composeTestRule.onNodeWithText("Cancel").assertHasClickAction()
    }

    @Test
    fun `when article link is clicked, then actionExitClicked is called`() {
        composeTestRule.onNodeWithText("Article").performClick()
        composeTestRule.onNodeWithText("Article").assertHasClickAction()
    }

    @Test
    fun `when webcast link is clicked, then actionExitClicked is called`() {
        composeTestRule.onNodeWithText("Webcast").performClick()
        composeTestRule.onNodeWithText("Webcast").assertHasClickAction()
    }

    @Test
    fun `when wikipedia link is clicked, then actionExitClicked is called`() {
        composeTestRule.onNodeWithText("Wikipedia").performClick()
        composeTestRule.onNodeWithText("Wikipedia").assertHasClickAction()
    }

    companion object {
        private const val DEFAULT_ARTICLE = "https://www.spacex.com"
        private const val DEFAULT_WEBCAST = "https://www.youtube.com"
        private const val DEFAULT_WIKI = "https://www.wikipedia.com"

        private val bottomSheetLinks = listOf(
            BottomSheetLinksUi(
                link = DEFAULT_ARTICLE,
                nameResId = R.string.article
            ),
            BottomSheetLinksUi(
                link = DEFAULT_WEBCAST,
                nameResId = R.string.webcast
            ),
            BottomSheetLinksUi(
                link = DEFAULT_WIKI,
                nameResId = R.string.wikipedia
            ),
        )
    }
}
