package com.seancoyle.launch.implementation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.seancoyle.core_ui.composables.TAG_LOADING
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterialApi
internal class LaunchScreenTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    @Test
    fun displayLoadingSpinner() {
        composeTestRule.setContent {
            val refreshing = rememberPullRefreshState(
                refreshing = false,
                onRefresh = {}
            )

           /* LaunchScreen(
                uiState = UiState,
                onChangeScrollPosition = {},
                loadNextPage = {},
                page = 0,
                pullRefreshState = refreshing,
                onCardClicked = {}
            )*/
        }

        composeTestRule.apply {
            onNodeWithTag(testTag = TAG_LOADING).assertExists()
        }
    }
}