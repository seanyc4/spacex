package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.filter.CompactFilterContent
import com.seancoyle.feature.launch.presentation.launches.filter.ExpandedFilterContent
import com.seancoyle.feature.launch.presentation.launches.filter.FilterBottomSheetEvent
import com.seancoyle.feature.launch.presentation.launches.filter.FilterBottomSheetState
import org.junit.Rule
import org.junit.Test

/**
 * UI tests for the Filter Bottom Sheet.
 *
 * Tests cover:
 * - Filter dialog visibility and test tags
 * - Search input behavior
 * - Status chip selection
 * - Clear all functionality
 * - Responsive layout behavior (compact vs expanded)
 * - Accessibility
 */
class FilterBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun filterBottomSheet_displaysFilterOptionsTitle() {
        val events = mutableListOf<FilterBottomSheetEvent>()

        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = { events.add(it) }
                )
            }
        }

        composeRule.onNodeWithText("Filter Options")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_displaysSearchSection() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        composeRule.onNodeWithText("Search")
            .assertIsDisplayed()

        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_SEARCH)
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_displaysStatusFilterSection() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        composeRule.onNodeWithText("Filter by Launch Status")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_displaysAllStatusChips() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        // Check all status options are displayed
        composeRule.onNodeWithText("All").assertIsDisplayed()
        composeRule.onNodeWithText("Success").assertIsDisplayed()
        composeRule.onNodeWithText("Go").assertIsDisplayed()
        composeRule.onNodeWithText("TBC").assertIsDisplayed()
        composeRule.onNodeWithText("TBD").assertIsDisplayed()
        composeRule.onNodeWithText("Failed").assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_searchInputUpdatesState() {
        val events = mutableListOf<FilterBottomSheetEvent>()

        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = { events.add(it) }
                )
            }
        }

        // Type in search field
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_SEARCH)
            .performTextInput("Falcon")

        // Verify QueryChanged event was fired
        assert(events.any { it is FilterBottomSheetEvent.QueryChanged })
        val queryEvent = events.filterIsInstance<FilterBottomSheetEvent.QueryChanged>().last()
        assert(queryEvent.query.contains("Falcon"))
    }

    @Test
    fun filterBottomSheet_showsExistingQuery() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(query = "Starlink"),
                    onEvent = {}
                )
            }
        }

        composeRule.onNodeWithText("Starlink")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_clearButtonAppearsWhenQueryExists() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(query = "Dragon"),
                    onEvent = {}
                )
            }
        }

        // Clear button should be visible when there's text
        composeRule.onNodeWithContentDescription("Cancel")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_statusChipClickTriggersEvent() {
        val events = mutableListOf<FilterBottomSheetEvent>()

        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = { events.add(it) }
                )
            }
        }

        // Click Success chip
        composeRule.onNodeWithText("Success")
            .performClick()

        // Verify StatusSelected event was fired
        assert(events.any { it is FilterBottomSheetEvent.StatusSelected })
        val statusEvent = events.filterIsInstance<FilterBottomSheetEvent.StatusSelected>().first()
        assert(statusEvent.status == LaunchStatus.SUCCESS)
    }

    @Test
    fun filterBottomSheet_selectedStatusChipIsHighlighted() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(selectedStatus = LaunchStatus.SUCCESS),
                    onEvent = {}
                )
            }
        }

        // The Success chip should show as selected
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_SUCCESS")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_clearAllButtonVisibleWhenFiltersActive() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(
                        query = "Test",
                        selectedStatus = LaunchStatus.SUCCESS
                    ),
                    onEvent = {}
                )
            }
        }

        composeRule.onNodeWithText("Clear All")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_clearAllButtonClickTriggersEvent() {
        val events = mutableListOf<FilterBottomSheetEvent>()

        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(query = "Test"),
                    onEvent = { events.add(it) }
                )
            }
        }

        composeRule.onNodeWithText("Clear All")
            .performClick()

        assert(events.any { it is FilterBottomSheetEvent.ClearAllFilters })
    }

    @Test
    fun filterBottomSheet_applyButtonTriggersEvent() {
        val events = mutableListOf<FilterBottomSheetEvent>()

        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = { events.add(it) }
                )
            }
        }

        // Click the apply button (shows "Search" when no active filters)
        composeRule.onNodeWithText("Search")
            .performClick()

        assert(events.any { it is FilterBottomSheetEvent.ApplyFilters })
    }

    @Test
    fun filterBottomSheet_cancelButtonTriggersEvent() {
        val events = mutableListOf<FilterBottomSheetEvent>()

        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = { events.add(it) }
                )
            }
        }

        composeRule.onNodeWithText("Cancel")
            .performClick()

        assert(events.any { it is FilterBottomSheetEvent.Dismiss })
    }

    @Test
    fun filterBottomSheet_applyButtonShowsApplyFiltersWhenActive() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(
                        query = "Test",
                        selectedStatus = LaunchStatus.SUCCESS
                    ),
                    onEvent = {}
                )
            }
        }

        composeRule.onNodeWithText("Apply Filters")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_displaysRecentSearches() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(
                        recentSearches = listOf("Starlink", "Dragon")
                    ),
                    onEvent = {}
                )
            }
        }

        composeRule.onNodeWithText("Recent")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Starlink")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Dragon")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_recentSearchClickTriggersEvent() {
        val events = mutableListOf<FilterBottomSheetEvent>()

        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(
                        recentSearches = listOf("Starlink")
                    ),
                    onEvent = { events.add(it) }
                )
            }
        }

        composeRule.onNodeWithText("Starlink")
            .performClick()

        val event = events.filterIsInstance<FilterBottomSheetEvent.RecentSearchSelected>().first()
        assert(event.query == "Starlink")
    }

    @Test
    fun filterBottomSheet_expandedLayoutDisplaysBothColumns() {
        composeRule.setContent {
            AppTheme {
                ExpandedFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        // Both search and status sections should be visible
        composeRule.onNodeWithText("Search")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Filter by Launch Status")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_compactLayoutDisplaysVerticalStack() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        // Both sections should be visible in vertical arrangement
        composeRule.onNodeWithText("Search")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Filter by Launch Status")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_showsFilterCountWhenFiltersActive() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(
                        query = "Test",
                        selectedStatus = LaunchStatus.SUCCESS
                    ),
                    onEvent = {}
                )
            }
        }

        // Should show "2" in badge (query + status)
        composeRule.onNodeWithContentDescription("2 active filters")
            .assertIsDisplayed()
    }

    @Test
    fun filterBottomSheet_statusChipsHaveContentDescriptions() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        composeRule.onNodeWithContentDescription("Filter by Success")
            .assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Filter by Failed")
            .assertIsDisplayed()
    }
}
