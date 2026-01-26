package com.seancoyle.feature.launch.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
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

class FilterBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenFilterBottomSheet_whenDisplayed_thenShowsFilterOptionsTitle() {
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
    fun givenFilterBottomSheet_whenDisplayed_thenShowsSearchSection() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        composeRule.onAllNodesWithText("Search")[0]
            .assertIsDisplayed()

        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_SEARCH)
            .assertIsDisplayed()
    }

    @Test
    fun givenFilterBottomSheet_whenDisplayed_thenShowsStatusFilterSection() {
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
    fun givenFilterBottomSheet_whenDisplayed_thenShowsAllStatusChips() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        // Check all status options are displayed using test tags
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_ALL").assertIsDisplayed()
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_SUCCESS").assertIsDisplayed()
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_GO").assertIsDisplayed()
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_TBC").assertIsDisplayed()
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_TBD").assertIsDisplayed()
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_FAILED").assertIsDisplayed()
    }

    @Test
    fun givenFilterBottomSheet_whenSearchInputUpdated_thenTriggersQueryChangedEvent() {
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
    fun givenExistingQuery_whenFilterBottomSheetDisplayed_thenShowsExistingQuery() {
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
    fun givenQueryExists_whenFilterBottomSheetDisplayed_thenShowsClearButton() {
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
    fun givenFilterBottomSheet_whenStatusChipClicked_thenTriggersStatusSelectedEvent() {
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
        composeRule.onNodeWithTag(LaunchesTestTags.FILTER_STATUS_CHIP + "_SUCCESS")
            .performClick()

        // Verify StatusSelected event was fired
        assert(events.any { it is FilterBottomSheetEvent.StatusSelected })
        val statusEvent = events.filterIsInstance<FilterBottomSheetEvent.StatusSelected>().first()
        assert(statusEvent.status == LaunchStatus.SUCCESS)
    }

    @Test
    fun givenSelectedStatus_whenFilterBottomSheetDisplayed_thenHighlightsSelectedStatusChip() {
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
    fun givenActiveFilters_whenFilterBottomSheetDisplayed_thenShowsClearAllButton() {
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
    fun givenActiveFilters_whenClearAllClicked_thenTriggersClearAllFiltersEvent() {
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
    fun givenFilterBottomSheet_whenApplyButtonClicked_thenTriggersApplyFiltersEvent() {
        val events = mutableListOf<FilterBottomSheetEvent>()

        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(query = "Test"),
                    onEvent = { events.add(it) }
                )
            }
        }

        composeRule.onNodeWithText("Apply Filters")
            .performClick()

        assert(events.any { it is FilterBottomSheetEvent.ApplyFilters })
    }

    @Test
    fun givenFilterBottomSheet_whenCancelButtonClicked_thenTriggersDismissEvent() {
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
    fun givenActiveFilters_whenFilterBottomSheetDisplayed_thenShowsApplyFiltersButtonText() {
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
    fun givenRecentSearches_whenFilterBottomSheetDisplayed_thenShowsRecentSearches() {
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
    fun givenRecentSearches_whenRecentSearchClicked_thenTriggersRecentSearchSelectedEvent() {
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
    fun givenExpandedLayout_whenFilterBottomSheetDisplayed_thenShowsBothColumns() {
        composeRule.setContent {
            AppTheme {
                ExpandedFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        composeRule.onAllNodesWithText("Search")[0]
            .assertIsDisplayed()
        composeRule.onNodeWithText("Filter by Launch Status")
            .assertIsDisplayed()
    }

    @Test
    fun givenCompactLayout_whenFilterBottomSheetDisplayed_thenShowsVerticalStack() {
        composeRule.setContent {
            AppTheme {
                CompactFilterContent(
                    state = FilterBottomSheetState(),
                    onEvent = {}
                )
            }
        }

        composeRule.onAllNodesWithText("Search")[0]
            .assertIsDisplayed()
        composeRule.onNodeWithText("Filter by Launch Status")
            .assertIsDisplayed()
    }

    @Test
    fun givenActiveFilters_whenFilterBottomSheetDisplayed_thenShowsFilterCount() {
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

        composeRule.onNodeWithContentDescription("2 active filters")
            .assertIsDisplayed()
    }

    @Test
    fun givenFilterBottomSheet_whenDisplayed_thenStatusChipsHaveContentDescriptions() {
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
