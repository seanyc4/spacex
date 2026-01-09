package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.components.SelectableLaunchCard
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import org.junit.Rule
import org.junit.Test

class SelectableLaunchCardTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun selectableLaunchCard_displaysContent() {
        val launch = LaunchesUi(
            id = "test-1",
            missionName = "Starlink Mission 42",
            launchDate = "January 15, 2026",
            status = LaunchStatus.GO,
            imageUrl = ""
        )

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    SelectableLaunchCard(
                        launchItem = launch,
                        onClick = { _, _ -> },
                        launchesType = LaunchesType.UPCOMING,
                        isSelected = false
                    )
                }
            }
        }

        composeRule.onNodeWithText("Starlink Mission 42")
            .assertIsDisplayed()
        composeRule.onNodeWithText("January 15, 2026")
            .assertIsDisplayed()
    }

    @Test
    fun selectableLaunchCard_invokesClickCallback() {
        var clickedId: String? = null
        var clickedType: LaunchesType? = null
        val launch = LaunchesUi(
            id = "test-click-id",
            missionName = "Test Mission",
            launchDate = "January 20, 2026",
            status = LaunchStatus.SUCCESS,
            imageUrl = ""
        )

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    SelectableLaunchCard(
                        launchItem = launch,
                        onClick = { id, type ->
                            clickedId = id
                            clickedType = type
                        },
                        launchesType = LaunchesType.PAST,
                        isSelected = false
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .performClick()

        assert(clickedId == "test-click-id") { "Expected clickedId to be 'test-click-id', but was $clickedId" }
        assert(clickedType == LaunchesType.PAST) { "Expected clickedType to be PAST, but was $clickedType" }
    }

    @Test
    fun selectableLaunchCard_showsSelectedState() {
        val launch = LaunchesUi(
            id = "selected-1",
            missionName = "Selected Mission",
            launchDate = "February 1, 2026",
            status = LaunchStatus.TBD,
            imageUrl = ""
        )

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    SelectableLaunchCard(
                        launchItem = launch,
                        onClick = { _, _ -> },
                        launchesType = LaunchesType.UPCOMING,
                        isSelected = true
                    )
                }
            }
        }

        // Verify card is displayed and has selected semantics
        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .assertIsDisplayed()

        // Check that the card has selected = true semantics
        composeRule.onNode(
            SemanticsMatcher.expectValue(SemanticsProperties.Selected, true)
        ).assertExists()
    }

    @Test
    fun selectableLaunchCard_showsUnselectedState() {
        val launch = LaunchesUi(
            id = "unselected-1",
            missionName = "Unselected Mission",
            launchDate = "February 5, 2026",
            status = LaunchStatus.GO,
            imageUrl = ""
        )

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    SelectableLaunchCard(
                        launchItem = launch,
                        onClick = { _, _ -> },
                        launchesType = LaunchesType.UPCOMING,
                        isSelected = false
                    )
                }
            }
        }

        // Check that the card has selected = false semantics
        composeRule.onNode(
            SemanticsMatcher.expectValue(SemanticsProperties.Selected, false)
        ).assertExists()
    }

    @Test
    fun selectableLaunchCard_displaysStatusChip() {
        val launch = LaunchesUi(
            id = "status-1",
            missionName = "Status Test Mission",
            launchDate = "March 1, 2026",
            status = LaunchStatus.SUCCESS,
            imageUrl = ""
        )

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    SelectableLaunchCard(
                        launchItem = launch,
                        onClick = { _, _ -> },
                        launchesType = LaunchesType.PAST,
                        isSelected = false
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.CARD_STATUS_CHIP)
            .assertIsDisplayed()
    }

    @Test
    fun selectableLaunchCard_hasCorrectTestTag() {
        val launch = LaunchesUi(
            id = "tag-test-1",
            missionName = "Tag Test Mission",
            launchDate = "March 10, 2026",
            status = LaunchStatus.GO,
            imageUrl = ""
        )

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    SelectableLaunchCard(
                        launchItem = launch,
                        onClick = { _, _ -> },
                        launchesType = LaunchesType.UPCOMING,
                        isSelected = false
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .assertIsDisplayed()
    }
}

