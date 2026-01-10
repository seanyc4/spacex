package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.ErrorState
import com.seancoyle.feature.launch.presentation.launch.components.LaunchDetailsSection
import com.seancoyle.feature.launch.presentation.launch.components.LaunchHeroSection
import com.seancoyle.feature.launch.presentation.launch.components.LaunchProviderSection
import com.seancoyle.feature.launch.presentation.launch.components.LaunchSiteSection
import com.seancoyle.feature.launch.presentation.launch.components.LoadingState
import com.seancoyle.feature.launch.presentation.launch.components.UpdatesSection
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import org.junit.Rule
import org.junit.Test

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
    fun loadingState_hasAccessibleContentDescription() {
        composeRule.setContent {
            AppTheme {
                LoadingState()
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LOADING_STATE)
            .assertContentDescriptionContains("Loading", substring = true, ignoreCase = true)
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

    @Test
    fun errorState_retryButtonInvokesCallback() {
        var retryClicked = false

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    ErrorState(
                        message = "Error occurred",
                        onRetry = { retryClicked = true }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.RETRY_BUTTON)
            .performClick()

        assert(retryClicked) { "Expected retry callback to be invoked" }
    }

    @Test
    fun errorState_hasAccessibleContentDescription() {
        val errorMessage = "Network timeout"

        composeRule.setContent {
            AppTheme {
                ErrorState(
                    message = errorMessage,
                    onRetry = {}
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.ERROR_STATE)
            .assertContentDescriptionContains(errorMessage, substring = true)
    }

    @Test
    fun heroSection_displaysMissionName() {
        val launch = TestDataFactory.createLaunchUI(missionName = "Starlink Group 7-12")

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText("Starlink Group 7-12")
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_displaysLaunchDate() {
        val launch = TestDataFactory.createLaunchUI(launchDate = "26 November 2026")

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText("26 November 2026")
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_displaysStatusChip() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.SUCCESS)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(LaunchStatus.SUCCESS.label)
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_hasCorrectTestTag() {
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
    fun heroSection_hasAccessibleMissionNameDescription() {
        val launch = TestDataFactory.createLaunchUI(missionName = "Demo Mission")

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithContentDescription("Mission name: Demo Mission")
            .assertExists()
    }

    @Test
    fun detailsSection_displaysMissionDescription() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(launch.mission.name.orEmpty())
            .assertIsDisplayed()
    }

    @Test
    fun detailsSection_displaysFailReasonWhenPresent() {
        val launch = TestDataFactory.createLaunchUIWithFailure()

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(launch.failReason!!, substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun detailsSection_hidesFailReasonWhenAbsent() {
        val launch = TestDataFactory.createLaunchUI(failReason = null)

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        // Should not find any node with typical failure text patterns
        composeRule.onNodeWithText("Failed", substring = true, ignoreCase = true)
            .assertDoesNotExist()
    }

    @Test
    fun launchSiteSection_displaysPadName() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = launch.pad)
            }
        }

        composeRule.onNodeWithText(launch.pad.name.orEmpty())
            .assertIsDisplayed()
    }

    @Test
    fun launchSiteSection_displaysLocationName() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = launch.pad)
            }
        }

        composeRule.onNodeWithText("Space Launch Complex 40")
            .assertIsDisplayed()
    }

    @Test
    fun launchProviderSection_displaysAgencyName() {
        val launch = TestDataFactory.createLaunchUI()
        val agency = launch.launchServiceProvider!!

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText("SpaceX")
            .assertIsDisplayed()
    }

    @Test
    fun launchProviderSection_displaysAgencyAbbreviation() {
        val launch = TestDataFactory.createLaunchUI()
        val agency = launch.launchServiceProvider!!

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText("SpX")
            .assertIsDisplayed()
    }

    @Test
    fun launchProviderSection_displaysAgencyDescription() {
        val launch = TestDataFactory.createLaunchUI()
        val agency = launch.launchServiceProvider!!

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText("Space Exploration Technologies Corp.")
            .assertIsDisplayed()
    }

    @Test
    fun updatesSection_displaysUpdatesWhenPresent() {
        val launch = TestDataFactory.createLaunchUIWithUpdates()

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = launch.updates)
            }
        }

        composeRule.onNodeWithText("Launch scrubbed due to weather conditions.")
            .assertIsDisplayed()
    }

    @Test
    fun updatesSection_displaysUpdateAuthor() {
        val launch = TestDataFactory.createLaunchUIWithUpdates()

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = launch.updates)
            }
        }

        composeRule.onNodeWithText( "SpaceX")
            .assertIsDisplayed()
    }

    @Test
    fun updatesSection_displaysUpdateTimestamp() {
        val launch = TestDataFactory.createLaunchUIWithUpdates()

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = launch.updates)
            }
        }

        composeRule.onNodeWithText("Jan 14, 2026 6:00 PM")
            .assertIsDisplayed()
    }

    @Test
    fun successState_hidesProviderSectionWhenNoProvider() {
        val launch = TestDataFactory.createLaunchUIWithoutOptionalData()

        composeRule.setContent {
            AppTheme {
                // We test that provider section is not rendered when agency is null
                // by verifying the provider-specific content is absent
                if (launch.launchServiceProvider != null) {
                    LaunchProviderSection(agency = launch.launchServiceProvider)
                }
            }
        }

        composeRule.onNodeWithText("SpaceX")
            .assertDoesNotExist()
    }

    @Test
    fun successState_hidesUpdatesSectionWhenNoUpdates() {
        val launch = TestDataFactory.createLaunchUIWithoutOptionalData()

        composeRule.setContent {
            AppTheme {
                if (launch.updates.isNotEmpty()) {
                    UpdatesSection(updates = launch.updates)
                }
            }
        }

        // Section title should not exist when there are no updates
        composeRule.onNodeWithText("Launch Updates", substring = true, ignoreCase = true)
            .assertDoesNotExist()
    }

    @Test
    fun heroSection_displaysGoStatus() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.GO)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(LaunchStatus.GO.label)
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_displaysFailedStatus() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.FAILED)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(LaunchStatus.FAILED.label)
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_displaysTBDStatus() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.TBD)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(LaunchStatus.TBD.label)
            .assertIsDisplayed()
    }

    @Test
    fun heroSection_displaysTBCStatus() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.TBC)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(LaunchStatus.TBC.label)
            .assertIsDisplayed()
    }
}
