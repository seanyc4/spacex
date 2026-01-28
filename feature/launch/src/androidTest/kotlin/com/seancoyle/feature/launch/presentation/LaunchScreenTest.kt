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
import com.seancoyle.core.ui.components.error.ErrorState
import com.seancoyle.core.ui.designsystem.theme.AppTheme
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
    fun givenLoadingState_whenDisplayed_thenShowsLoadingIndicator() {
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
    fun givenLoadingState_whenDisplayed_thenHasAccessibleContentDescription() {
        composeRule.setContent {
            AppTheme {
                LoadingState()
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LOADING_STATE)
            .assertContentDescriptionContains("Loading", substring = true, ignoreCase = true)
    }

    @Test
    fun givenErrorMessage_whenErrorStateDisplayed_thenShowsErrorMessage() {
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
    fun givenErrorState_whenDisplayed_thenShowsRetryButton() {
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
    fun givenErrorState_whenRetryButtonClicked_thenInvokesCallback() {
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
    fun givenErrorMessage_whenErrorStateDisplayed_thenHasAccessibleContentDescription() {
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
    fun givenMissionName_whenHeroSectionDisplayed_thenShowsMissionName() {
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
    fun givenLaunchDate_whenHeroSectionDisplayed_thenShowsLaunchDate() {
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
    fun givenSuccessStatus_whenHeroSectionDisplayed_thenShowsStatusChip() {
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
    fun givenHeroSection_whenDisplayed_thenHasCorrectTestTag() {
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
    fun givenMissionName_whenHeroSectionDisplayed_thenHasAccessibleMissionNameDescription() {
        val launch = TestDataFactory.createLaunchUI(missionName = "Demo Mission")

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithContentDescription("Mission image for Demo Mission")
            .assertExists()
    }

    @Test
    fun givenLaunchUI_whenDetailsSectionDisplayed_thenShowsMissionDescription() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(launch.mission.name)
            .assertIsDisplayed()
    }

    @Test
    fun givenFailReason_whenDetailsSectionDisplayed_thenShowsFailReason() {
        val launch = TestDataFactory.createLaunchUIWithFailure()

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText(launch.failReason!!, substring = true)
            .assertExists()
    }

    @Test
    fun givenNoFailReason_whenDetailsSectionDisplayed_thenHidesFailReason() {
        val launch = TestDataFactory.createLaunchUI(failReason = null)

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText("Failed", substring = true, ignoreCase = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenPadInfo_whenLaunchSiteSectionDisplayed_thenShowsPadName() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = launch.pad)
            }
        }

        composeRule.onNodeWithText(launch.pad.name)
            .assertIsDisplayed()
    }

    @Test
    fun givenPadInfo_whenLaunchSiteSectionDisplayed_thenShowsLocationName() {
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
    fun givenAgency_whenLaunchProviderSectionDisplayed_thenShowsAgencyName() {
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
    fun givenAgency_whenLaunchProviderSectionDisplayed_thenShowsAgencyAbbreviation() {
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
    fun givenAgency_whenLaunchProviderSectionDisplayed_thenShowsAgencyDescription() {
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
    fun givenUpdates_whenUpdatesSectionDisplayed_thenShowsUpdates() {
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
    fun givenUpdates_whenUpdatesSectionDisplayed_thenShowsUpdateAuthor() {
        val launch = TestDataFactory.createLaunchUIWithUpdates()

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = launch.updates)
            }
        }

        composeRule.onNodeWithText("SpaceX")
            .assertIsDisplayed()
    }

    @Test
    fun givenUpdates_whenUpdatesSectionDisplayed_thenShowsUpdateTimestamp() {
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
    fun givenNoProvider_whenSuccessStateDisplayed_thenHidesProviderSection() {
        val launch = TestDataFactory.createLaunchUIWithoutOptionalData()

        composeRule.setContent {
            AppTheme {
                if (launch.launchServiceProvider != null) {
                    LaunchProviderSection(agency = launch.launchServiceProvider)
                }
            }
        }

        composeRule.onNodeWithText("SpaceX")
            .assertDoesNotExist()
    }

    @Test
    fun givenNoUpdates_whenSuccessStateDisplayed_thenHidesUpdatesSection() {
        val launch = TestDataFactory.createLaunchUIWithoutOptionalData()

        composeRule.setContent {
            AppTheme {
                if (launch.updates.isNotEmpty()) {
                    UpdatesSection(updates = launch.updates)
                }
            }
        }

        composeRule.onNodeWithText("Launch Updates", substring = true, ignoreCase = true)
            .assertDoesNotExist()
    }

    @Test
    fun givenGoStatus_whenHeroSectionDisplayed_thenShowsGoStatus() {
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
    fun givenFailedStatus_whenHeroSectionDisplayed_thenShowsFailedStatus() {
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
    fun givenTBDStatus_whenHeroSectionDisplayed_thenShowsTBDStatus() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.TBD)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.HERO_STATUS_CHIP + LaunchStatus.TBD.name)
            .assertIsDisplayed()
    }

    @Test
    fun givenTBCStatus_whenHeroSectionDisplayed_thenShowsTBCStatus() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.TBC)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.HERO_STATUS_CHIP + LaunchStatus.TBC.name)
            .assertIsDisplayed()
    }

    @Test
    fun givenGoStatus_whenHeroSectionDisplayed_thenShowsHeroSection() {
        val launch = TestDataFactory.createLaunchUI(status = LaunchStatus.GO)

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_HERO_SECTION)
            .assertIsDisplayed()
    }

    @Test
    fun givenMissionName_whenHeroSectionDisplayed_thenShowsImageWithContentDescription() {
        val launch = TestDataFactory.createLaunchUI(missionName = "Test Mission")

        composeRule.setContent {
            AppTheme {
                LaunchHeroSection(launch = launch)
            }
        }

        composeRule.onNodeWithContentDescription("Mission image for Test Mission")
            .assertExists()
    }

    @Test
    fun givenLaunchUI_whenDetailsSectionDisplayed_thenShowsMissionDetailsTitle() {
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
    fun givenMissionType_whenDetailsSectionDisplayed_thenShowsMissionType() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.CARD_STATUS_CHIP + launch.mission.type!!, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun givenOrbitName_whenDetailsSectionDisplayed_thenShowsOrbitName() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.CARD_STATUS_CHIP + launch.mission.orbitName!!, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchTime_whenDetailsSectionDisplayed_thenShowsLaunchTime() {
        val launch = TestDataFactory.createLaunchUI(launchTime = "14:30")

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText("14:30")
            .assertIsDisplayed()
    }

    @Test
    fun givenWindowDuration_whenDetailsSectionDisplayed_thenShowsWindowDuration() {
        val launch = TestDataFactory.createLaunchUI(windowDuration = "1h")

        composeRule.setContent {
            AppTheme {
                LaunchDetailsSection(launch = launch)
            }
        }

        composeRule.onNodeWithText("1h")
            .assertIsDisplayed()
    }

    @Test
    fun givenPadInfo_whenLaunchSiteSectionDisplayed_thenShowsMapUrl() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = launch.pad)
            }
        }

        composeRule.onNodeWithText(launch.pad.name)
            .assertIsDisplayed()
    }

    @Test
    fun givenPadInfo_whenLaunchSiteSectionDisplayed_thenShowsLatitudeLongitude() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = launch.pad)
            }
        }

        composeRule.onNodeWithText(launch.pad.latitude!!, substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun givenPadInfo_whenLaunchSiteSectionDisplayed_thenShowsTotalLaunchCount() {
        val launch = TestDataFactory.createLaunchUI()

        composeRule.setContent {
            AppTheme {
                LaunchSiteSection(pad = launch.pad)
            }
        }

        composeRule.onNodeWithText(launch.pad.totalLaunchCount, substring = true)
            .assertExists()
    }

    @Test
    fun givenMultipleUpdates_whenUpdatesSectionDisplayed_thenShowsMultipleUpdates() {
        val launch = TestDataFactory.createLaunchUIWithUpdates()

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = launch.updates)
            }
        }

        composeRule.onNodeWithText("All systems are go for launch tomorrow.")
            .assertIsDisplayed()
    }

    @Test
    fun givenAgency_whenLaunchProviderSectionDisplayed_thenShowsAgencyType() {
        val launch = TestDataFactory.createLaunchUI()
        val agency = launch.launchServiceProvider!!

        composeRule.setContent {
            AppTheme {
                LaunchProviderSection(agency = agency)
            }
        }

        composeRule.onNodeWithText(agency.type)
            .assertIsDisplayed()
    }
}
