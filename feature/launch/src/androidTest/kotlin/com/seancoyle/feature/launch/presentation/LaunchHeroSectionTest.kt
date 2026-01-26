package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.LaunchHeroSection
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import org.junit.Rule
import org.junit.Test

class LaunchHeroSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenLaunchHeroSection_whenDisplayed_thenHasCorrectTestTag() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_HERO_SECTION)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchHeroSection_whenDisplayed_thenShowsMissionName() {
        val missionName = "Starlink Group 7-12"
        val launch = TestDataFactory.createLaunchUI(missionName = missionName)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(missionName)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchHeroSection_whenDisplayed_thenShowsLaunchDate() {
        val launchDate = "26 November 2026"
        val launch = TestDataFactory.createLaunchUI(launchDate = launchDate)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(launchDate)
            .assertIsDisplayed()
    }

    @Test
    fun givenSuccessStatus_whenLaunchHeroSectionDisplayed_thenShowsSuccessChip() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.SUCCESS)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.HERO_STATUS_CHIP + LaunchStatus.SUCCESS.name)
            .assertIsDisplayed()
    }

    @Test
    fun givenGoStatus_whenLaunchHeroSectionDisplayed_thenShowsGoChip() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.GO)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.HERO_STATUS_CHIP + LaunchStatus.GO.name)
            .assertIsDisplayed()
    }

    @Test
    fun givenFailedStatus_whenLaunchHeroSectionDisplayed_thenShowsFailedChip() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.FAILED)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.HERO_STATUS_CHIP + LaunchStatus.FAILED.name)
            .assertIsDisplayed()
    }

    @Test
    fun givenTbdStatus_whenLaunchHeroSectionDisplayed_thenShowsTbdChip() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.TBD)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.HERO_STATUS_CHIP + LaunchStatus.TBD.name)
            .assertIsDisplayed()
    }
}
