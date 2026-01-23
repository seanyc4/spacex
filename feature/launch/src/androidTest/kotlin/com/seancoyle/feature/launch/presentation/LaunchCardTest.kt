package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.components.LaunchCard
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import org.junit.Rule
import org.junit.Test

class LaunchCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenMissionName_whenLaunchCardDisplayed_thenShowsMissionName() {
        val missionName = "Starlink Mission"

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(missionName = missionName),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithText(missionName)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchDate_whenLaunchCardDisplayed_thenShowsLaunchDate() {
        val launchDate = "January 15, 2026"

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(launchDate = launchDate),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithText(launchDate)
            .assertIsDisplayed()
    }

    @Test
    fun givenSuccessStatus_whenLaunchCardDisplayed_thenShowsStatusChip() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(status = LaunchStatus.SUCCESS),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.PAST,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.CARD_STATUS_CHIP)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchCard_whenDisplayed_thenHasCorrectTestTag() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .assertIsDisplayed()
    }

    @Test
    fun givenSuccessStatus_whenLaunchCardDisplayed_thenShowsSuccessStatusAbbrev() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(status = LaunchStatus.SUCCESS),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.PAST,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithText(LaunchStatus.SUCCESS.abbrev)
            .assertIsDisplayed()
    }

    @Test
    fun givenGoStatus_whenLaunchCardDisplayed_thenShowsGoStatusAbbrev() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(status = LaunchStatus.GO),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithText(LaunchStatus.GO.abbrev)
            .assertIsDisplayed()
    }

    @Test
    fun givenFailedStatus_whenLaunchCardDisplayed_thenShowsFailedStatusAbbrev() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(status = LaunchStatus.FAILED),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.PAST,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithText(LaunchStatus.FAILED.abbrev)
            .assertIsDisplayed()
    }

    @Test
    fun givenTBDStatus_whenLaunchCardDisplayed_thenShowsTBDStatusAbbrev() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(status = LaunchStatus.TBD),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithText(LaunchStatus.TBD.abbrev)
            .assertIsDisplayed()
    }

    @Test
    fun givenTBCStatus_whenLaunchCardDisplayed_thenShowsTBCStatusAbbrev() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(status = LaunchStatus.TBC),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithText(LaunchStatus.TBC.abbrev)
            .assertIsDisplayed()
    }

    @Test
    fun givenLaunchId_whenLaunchCardClicked_thenTriggersOnClickWithCorrectId() {
        var clickedId: String? = null
        var clickedType: LaunchesType? = null
        val testId = "test-launch-id"

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(id = testId),
                    onClick = { id, type ->
                        clickedId = id
                        clickedType = type
                    },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .performClick()

        assert(clickedId == testId) { "Expected id $testId but got $clickedId" }
        assert(clickedType == LaunchesType.UPCOMING) { "Expected UPCOMING but got $clickedType" }
    }

    @Test
    fun givenPastLaunchType_whenLaunchCardClicked_thenTriggersOnClickWithPastType() {
        var clickedType: LaunchesType? = null

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(),
                    onClick = { _, type -> clickedType = type },
                    launchesType = LaunchesType.PAST,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .performClick()

        assert(clickedType == LaunchesType.PAST) { "Expected PAST but got $clickedType" }
    }

    @Test
    fun givenMissionName_whenLaunchCardDisplayed_thenHasAccessibleContentDescription() {
        val missionName = "Starlink Mission"
        val launchDate = "January 15, 2026"

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(
                        missionName = missionName,
                        launchDate = launchDate
                    ),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        // Card should have content description containing mission info
        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .assertContentDescriptionContains(missionName, substring = true)
    }

    @Test
    fun givenGoStatus_whenLaunchCardDisplayed_thenStatusChipHasMeaningfulContent() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(status = LaunchStatus.GO),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        // Status chip should display the status abbreviation
        composeRule.onNode(hasTestTag(LaunchesTestTags.CARD_STATUS_CHIP) and hasText("Go"))
            .assertIsDisplayed()
    }

    @Test
    fun givenLongMissionName_whenLaunchCardDisplayed_thenRendersProperlyWithLongText() {
        val longMissionName = "Very Long Mission Name That Should Be Truncated Properly"

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(missionName = longMissionName),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true
                )
            }
        }

        // Card should still render properly with long text
        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .assertIsDisplayed()
    }

    private fun createTestLaunchesUi(
        id: String = "test-id",
        missionName: String = "Test Mission",
        launchDate: String = "January 1, 2026",
        status: LaunchStatus = LaunchStatus.GO,
        imageUrl: String = "https://example.com/image.jpg",
        location: String = "United State of America"
    ): LaunchesUi = LaunchesUi(
        id = id,
        missionName = missionName,
        launchDate = launchDate,
        status = status,
        imageUrl = imageUrl,
        location = location
    )
}
