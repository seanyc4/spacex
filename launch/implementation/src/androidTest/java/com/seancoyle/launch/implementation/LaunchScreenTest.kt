package com.seancoyle.launch.implementation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.launch.implementation.presentation.LaunchEvents
import com.seancoyle.launch.implementation.presentation.LaunchRoute
import com.seancoyle.launch.implementation.presentation.LaunchScreen
import org.junit.Rule
import org.junit.Test

@ExperimentalMaterialApi
class LaunchScreenTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    @Test
    fun initialStateIsRendered() {
        composeTestRule.setContent {
            val refreshing = rememberPullRefreshState(
                refreshing = false,
                onRefresh = {}
            )

            LaunchScreen(
                launchItems = emptyList(),
                loading = true,
                onChangeScrollPosition = {},
                loadNextPage = {},
                page = 0 ,
                pullRefreshState = refreshing,
                onCardClicked = {}
            )
        }

        composeTestRule.apply {

        }
    }
}