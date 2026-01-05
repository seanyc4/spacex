package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.ErrorState
import com.seancoyle.feature.launch.presentation.launch.components.LoadingState
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for LaunchScreen components.
 * Tests the different states (Loading, Success, Error) of the launch detail screen.
 *
 * These tests verify the UI behavior of the launch screen components
 * using Compose testing framework.
 */
class LaunchScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loadingState_displaysLoadingIndicator() {
        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    LoadingState()
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LOADING_STATE)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displaysErrorMessage() {
        val errorMessage = "Unable to connect to server"

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    ErrorState(
                        message = errorMessage,
                        onRetry = {}
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.ERROR_STATE)
            .assertIsDisplayed()
        composeRule.onNodeWithText(errorMessage)
            .assertIsDisplayed()
    }

    @Test
    fun errorState_displaysRetryButton() {
        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    ErrorState(
                        message = "Error occurred",
                        onRetry = {}
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.RETRY_BUTTON)
            .assertIsDisplayed()
    }
}
