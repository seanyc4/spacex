package com.seancoyle.launch.implementation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.seancoyle.core_ui.composables.TAG_LOADING
import com.seancoyle.launch.implementation.presentation.LaunchScreen
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterialApi
class LaunchScreenTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    @Test
    fun displayLoadingSpinner() {
        composeTestRule.setContent {
            val refreshing = rememberPullRefreshState(
                refreshing = false,
                onRefresh = {}
            )

            LaunchScreen(
                uiState = emptyList(),
                loading = true,
                onChangeScrollPosition = {},
                loadNextPage = {},
                page = 0 ,
                pullRefreshState = refreshing,
                onItemClicked = {}
            )
        }

        composeTestRule.apply {
            onNodeWithTag(testTag = TAG_LOADING).assertExists()
        }
    }
}