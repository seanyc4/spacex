package com.seancoyle.feature.launch.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Rocket
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.components.DetailRow
import com.seancoyle.feature.launch.presentation.launch.components.SectionTitle
import org.junit.Rule
import org.junit.Test

class CommonComponentsTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenSectionTitle_whenDisplayed_thenShowsTitleText() {
        val titleText = "Mission Details"

        composeRule.setContent {
            AppTheme {
                SectionTitle(text = titleText)
            }
        }

        composeRule.onNodeWithText(titleText)
            .assertIsDisplayed()
    }

    @Test
    fun givenSectionTitle_whenDisplayed_thenHasCorrectTestTag() {
        composeRule.setContent {
            AppTheme {
                SectionTitle(text = "Test Title")
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.SECTION_TITLE)
            .assertIsDisplayed()
    }

    @Test
    fun givenDetailRow_whenDisplayed_thenShowsLabel() {
        val label = "Launch Date"
        val value = "January 15, 2026"

        composeRule.setContent {
            AppTheme {
                DetailRow(
                    label = label,
                    value = value,
                    icon = Icons.Default.Info
                )
            }
        }

        composeRule.onNodeWithText(label)
            .assertIsDisplayed()
    }

    @Test
    fun givenDetailRow_whenDisplayed_thenShowsValue() {
        val label = "Launch Date"
        val value = "January 15, 2026"

        composeRule.setContent {
            AppTheme {
                DetailRow(
                    label = label,
                    value = value,
                    icon = Icons.Default.Info
                )
            }
        }

        composeRule.onNodeWithText(value)
            .assertIsDisplayed()
    }

    @Test
    fun givenDetailRow_whenDisplayed_thenHasCorrectTestTag() {
        composeRule.setContent {
            AppTheme {
                DetailRow(
                    label = "Status",
                    value = "Success",
                    icon = Icons.Default.Rocket
                )
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.DETAIL_ROW)
            .assertIsDisplayed()
    }

    @Test
    fun givenMultipleDetailRows_whenDisplayed_thenAllRowsAreVisible() {
        composeRule.setContent {
            AppTheme {
                DetailRow(
                    label = "First Label",
                    value = "First Value",
                    icon = Icons.Default.Info
                )
                DetailRow(
                    label = "Second Label",
                    value = "Second Value",
                    icon = Icons.Default.Rocket
                )
            }
        }

        composeRule.onNodeWithText("First Label")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Second Label")
            .assertIsDisplayed()
    }
}
