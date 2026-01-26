package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
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
    fun givenLaunch_whenLaunchCardDisplayed_thenShowsMissionNameMissionStatusAndMissionDate() {
        val missionName = "Starlink Mission"

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(missionName = missionName),
                    onClick = { _, _, _ -> },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true,
                    position = 0
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD + "_test-id")
            .assertContentDescriptionContains("Starlink Mission, Go for Launch, launching January 1, 2026")
    }


    @Test
    fun givenLaunchId_whenLaunchCardClicked_thenTriggersOnClickWithCorrectId() {
        val testId = "test-launch-id"
        var clickedLaunch = createTestLaunchesUi(id = testId)
        var clickedType: LaunchesType? = null

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = clickedLaunch,
                    onClick = { launchItem, launchesType, _ ->
                        clickedType = launchesType
                        clickedLaunch = launchItem
                    },
                    launchesType = LaunchesType.UPCOMING,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true,
                    position = 0
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD + "_test-launch-id")
            .performClick()

        assert(clickedLaunch.id == testId) { "Expected id $testId but got ${clickedLaunch.id}" }
        assert(clickedType == LaunchesType.UPCOMING) { "Expected UPCOMING but got $clickedType" }
    }

    @Test
    fun givenPastLaunchType_whenLaunchCardClicked_thenTriggersOnClickWithPastType() {
        var clickedType: LaunchesType? = null

        composeRule.setContent {
            AppTheme {
                LaunchCard(
                    launchItem = createTestLaunchesUi(),
                    onClick = { _, type, _ -> clickedType = type },
                    launchesType = LaunchesType.PAST,
                    modifier = Modifier.padding(16.dp),
                    isSelected = true,
                    position = 0
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD + "_test-id")
            .performClick()

        assert(clickedType == LaunchesType.PAST) { "Expected PAST but got $clickedType" }
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
