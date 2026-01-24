package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.UpdatesSection
import com.seancoyle.feature.launch.presentation.launch.model.LaunchUpdateUI
import org.junit.Rule
import org.junit.Test

class UpdatesSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenUpdates_whenDisplayed_thenShowsSectionTitle() {
        val updates = listOf(createTestUpdate())

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithText("Launch Updates")
            .assertIsDisplayed()
    }

    @Test
    fun givenSingleUpdate_whenDisplayed_thenShowsUpdateComment() {
        val comment = "Launch scrubbed due to weather"
        val updates = listOf(createTestUpdate(comment = comment))

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithText(comment)
            .assertIsDisplayed()
    }

    @Test
    fun givenSingleUpdate_whenDisplayed_thenShowsCreatedBy() {
        val createdBy = "SpaceX"
        val updates = listOf(createTestUpdate(createdBy = createdBy))

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithText(createdBy)
            .assertIsDisplayed()
    }

    @Test
    fun givenSingleUpdate_whenDisplayed_thenShowsCreatedOn() {
        val createdOn = "Jan 15, 2026 10:00 AM"
        val updates = listOf(createTestUpdate(createdOn = createdOn))

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithText(createdOn)
            .assertIsDisplayed()
    }

    @Test
    fun givenMultipleUpdates_whenDisplayed_thenShowsAllVisibleUpdates() {
        val updates = listOf(
            createTestUpdate(comment = "Update One"),
            createTestUpdate(comment = "Update Two"),
            createTestUpdate(comment = "Update Three")
        )

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithText("Update One")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Update Two")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Update Three")
            .assertIsDisplayed()
    }

    @Test
    fun givenMoreThanFiveUpdates_whenDisplayed_thenShowsExpandIcon() {
        val updates = (1..7).map { createTestUpdate(comment = "Update $it") }

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithContentDescription("Expand")
            .assertIsDisplayed()
    }

    @Test
    fun givenFiveOrFewerUpdates_whenDisplayed_thenHidesExpandIcon() {
        val updates = (1..3).map { createTestUpdate(comment = "Update $it") }

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithContentDescription("Expand")
            .assertDoesNotExist()
    }

    @Test
    fun givenCollapsedState_whenExpandClicked_thenShowsAllUpdates() {
        val updates = (1..7).map { createTestUpdate(comment = "Update $it") }

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithText("Update 6")
            .assertDoesNotExist()

        composeRule.onNodeWithContentDescription("Expand")
            .performClick()

        composeRule.onNodeWithText("Update 6")
            .assertIsDisplayed()
    }

    @Test
    fun givenExpandedState_whenCollapseClicked_thenHidesExtraUpdates() {
        val updates = (1..7).map { createTestUpdate(comment = "Update $it") }

        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = updates)
            }
        }

        composeRule.onNodeWithContentDescription("Expand")
            .performClick()

        composeRule.onNodeWithText("Update 6")
            .assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Collapse")
            .performClick()

        composeRule.onNodeWithText("Update 6")
            .assertDoesNotExist()
    }

    @Test
    fun givenEmptyUpdates_whenDisplayed_thenShowsSectionTitle() {
        composeRule.setContent {
            AppTheme {
                UpdatesSection(updates = emptyList())
            }
        }

        composeRule.onNodeWithText("Launch Updates")
            .assertIsDisplayed()
    }

    private fun createTestUpdate(
        comment: String = "Test update comment",
        createdBy: String = "Test Author",
        createdOn: String = "Jan 1, 2026 12:00 PM"
    ): LaunchUpdateUI = LaunchUpdateUI(
        comment = comment,
        createdBy = createdBy,
        createdOn = createdOn
    )
}
