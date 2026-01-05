package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.components.LaunchCard
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for LaunchCard component.
 * Tests the launch card displays correct information and handles interactions.
 */
class LaunchCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun launchCard_displaysMissionName() {
        val missionName = "Starlink Mission"

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(missionName = missionName),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        composeRule.onNodeWithText(missionName)
            .assertIsDisplayed()
    }

    @Test
    fun launchCard_displaysLaunchDate() {
        val launchDate = "January 15, 2026"

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(launchDate = launchDate),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        composeRule.onNodeWithText(launchDate)
            .assertIsDisplayed()
    }

    @Test
    fun launchCard_displaysStatusChip() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(status = LaunchStatus.SUCCESS),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.PAST,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.CARD_STATUS_CHIP)
            .assertIsDisplayed()
    }

    @Test
    fun launchCard_hasCorrectTestTag() {
        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(),
                    onClick = { _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .assertIsDisplayed()
    }

    @Test
    fun launchCard_triggersOnClickWithCorrectId() {
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
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .performClick()

        assert(clickedId == testId) { "Expected id $testId but got $clickedId" }
        assert(clickedType == LaunchesType.UPCOMING) { "Expected UPCOMING but got $clickedType" }
    }

    private fun createTestLaunchesUi(
        id: String = "test-id",
        missionName: String = "Test Mission",
        launchDate: String = "January 1, 2026",
        status: LaunchStatus = LaunchStatus.GO,
        imageUrl: String = "https://example.com/image.jpg"
    ): LaunchesUi {
        return LaunchesUi(
            id = id,
            missionName = missionName,
            launchDate = launchDate,
            status = status,
            imageUrl = imageUrl
        )
    }
}
