package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.LaunchDetailsSection
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import org.junit.Rule
import org.junit.Test

class MissionDetailsSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenLaunchDetailsSection_whenDisplayed_thenShowsMissionDetailsTitle() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText("Mission Details")
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchDetailsSection_whenDisplayed_thenShowsMissionName() {
        val missionName = "Starlink Group 7-12"
        val launch = TestDataFactory.createLaunchUI(missionName = missionName)

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(missionName)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchDetailsSection_whenDisplayed_thenShowsLaunchDate() {
        val launchDate = "26 November 2026"
        val launch = TestDataFactory.createLaunchUI(launchDate = launchDate)

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(launchDate)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchDetailsSection_whenDisplayed_thenShowsLaunchTime() {
        val launchTime = "10:30"
        val launch = TestDataFactory.createLaunchUI(launchTime = launchTime)

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(launchTime)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchWithWindowTimes_whenDisplayed_thenShowsWindowStartTime() {
        val windowStartTime = "10:00"
        val launch = TestDataFactory.createLaunchUI(windowStartTime = windowStartTime)

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(windowStartTime)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchWithWindowTimes_whenDisplayed_thenShowsWindowEndTime() {
        val windowEndTime = "11:00"
        val launch = TestDataFactory.createLaunchUI(windowEndTime = windowEndTime)

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(windowEndTime)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchWithFailReason_whenDisplayed_thenShowsFailReason() {
        val failReason = "Engine malfunction during ascent"
        val launch = TestDataFactory.createLaunchUI(
            status = LaunchStatus.FAILED,
            failReason = failReason
        )

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(failReason)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchWithoutFailReason_whenDisplayed_thenDoesNotShowFailSection() {
        val launch = TestDataFactory.createLaunchUI(
            status = LaunchStatus.SUCCESS,
            failReason = null
        )

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText("Failure Reason", substring = true)
            .assertDoesNotExist()
    }
}
