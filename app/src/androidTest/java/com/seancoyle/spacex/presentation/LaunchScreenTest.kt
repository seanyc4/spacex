package com.seancoyle.spacex.presentation

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import com.seancoyle.launch.implementation.presentation.LaunchScreen
import com.seancoyle.launch.implementation.presentation.LaunchViewModel
import io.mockk.impl.annotations.MockK
import org.junit.Rule
import org.junit.Test

class LaunchScreenTest {

    @get:Rule
    val composeTestRule: ComposeContentTestRule = createComposeRule()

    @MockK
    private lateinit var viewModel: LaunchViewModel

    @MockK
    private lateinit var pullRefreshState: PullRefreshState

    @Test
    fun initialStateIsRendered() {
        composeTestRule.setContent {
            LaunchScreen(
                viewModel = viewModel,
                refreshState =,
                onCardClicked = {})
        }
    }
}