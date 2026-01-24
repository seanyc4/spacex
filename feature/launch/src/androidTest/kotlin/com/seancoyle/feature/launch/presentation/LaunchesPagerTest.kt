package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.LaunchesScreen
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvent
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesUiState
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class LaunchesPagerTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun givenUpcomingTabSelected_whenDisplayed_thenUpcomingTabIsHighlighted() {
        val upcomingLaunches = createTestLaunches(count = 3)
        val pastLaunches = createTestLaunches(count = 2)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val upcomingPagingItems =
                        flowOf(PagingData.from(upcomingLaunches)).collectAsLazyPagingItems()
                    val pastPagingItems =
                        flowOf(PagingData.from(pastLaunches)).collectAsLazyPagingItems()

                    LaunchesScreen(
                        upcomingFeedState = upcomingPagingItems,
                        pastFeedState = pastPagingItems,
                        state = LaunchesUiState(launchesType = LaunchesType.UPCOMING),
                        isRefreshing = false,
                        columnCount = 1,
                        selectedLaunchId = null,
                        onEvent = {},
                        onUpdateScrollPosition = { _, _ -> },
                        onClick = { _, _, _ -> }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.UPCOMING_TAB)
            .assertIsDisplayed()
    }

    @Test
    fun givenPastTabSelected_whenDisplayed_thenPastTabIsHighlighted() {
        val upcomingLaunches = createTestLaunches(count = 3)
        val pastLaunches = createTestLaunches(count = 2)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val upcomingPagingItems =
                        flowOf(PagingData.from(upcomingLaunches)).collectAsLazyPagingItems()
                    val pastPagingItems =
                        flowOf(PagingData.from(pastLaunches)).collectAsLazyPagingItems()

                    LaunchesScreen(
                        upcomingFeedState = upcomingPagingItems,
                        pastFeedState = pastPagingItems,
                        state = LaunchesUiState(launchesType = LaunchesType.PAST),
                        isRefreshing = false,
                        columnCount = 1,
                        selectedLaunchId = null,
                        onEvent = {},
                        onUpdateScrollPosition = { _, _ -> },
                        onClick = { _, _, _ -> }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.PAST_TAB)
            .assertIsDisplayed()
    }

    @Test
    fun givenUpcomingTab_whenPastTabClicked_thenTabSelectedEventFired() {
        val upcomingLaunches = createTestLaunches(count = 3)
        val pastLaunches = createTestLaunches(count = 2)
        var selectedType: LaunchesType? = null

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val upcomingPagingItems =
                        flowOf(PagingData.from(upcomingLaunches)).collectAsLazyPagingItems()
                    val pastPagingItems =
                        flowOf(PagingData.from(pastLaunches)).collectAsLazyPagingItems()

                    LaunchesScreen(
                        upcomingFeedState = upcomingPagingItems,
                        pastFeedState = pastPagingItems,
                        state = LaunchesUiState(launchesType = LaunchesType.UPCOMING),
                        isRefreshing = false,
                        columnCount = 1,
                        selectedLaunchId = null,
                        onEvent = { event ->
                            if (event is LaunchesEvent.TabSelected) {
                                selectedType = event.launchesType
                            }
                        },
                        onUpdateScrollPosition = { _, _ -> },
                        onClick = { _, _, _ -> }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.PAST_TAB)
            .performClick()

        assert(selectedType == LaunchesType.PAST)
    }


    @Test
    fun givenBothTabs_whenDisplayed_thenBothTabsAreVisible() {
        val upcomingLaunches = createTestLaunches(count = 1)
        val pastLaunches = createTestLaunches(count = 1)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val upcomingPagingItems =
                        flowOf(PagingData.from(upcomingLaunches)).collectAsLazyPagingItems()
                    val pastPagingItems =
                        flowOf(PagingData.from(pastLaunches)).collectAsLazyPagingItems()

                    LaunchesScreen(
                        upcomingFeedState = upcomingPagingItems,
                        pastFeedState = pastPagingItems,
                        state = LaunchesUiState(launchesType = LaunchesType.UPCOMING),
                        isRefreshing = false,
                        columnCount = 1,
                        selectedLaunchId = null,
                        onEvent = {},
                        onUpdateScrollPosition = { _, _ -> },
                        onClick = { _, _, _ -> }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.UPCOMING_TAB)
            .assertIsDisplayed()
        composeRule.onNodeWithTag(LaunchesTestTags.PAST_TAB)
            .assertIsDisplayed()
    }

    private fun createTestLaunches(count: Int): List<LaunchesUi> {
        return (0 until count).map { index ->
            createTestLaunchesUi(
                id = "id-$index",
                missionName = "Mission $index"
            )
        }
    }

    private fun createTestLaunchesUi(
        id: String = "test-id",
        missionName: String = "Test Mission",
        launchDate: String = "January 1, 2026",
        status: LaunchStatus = LaunchStatus.GO,
        imageUrl: String = "https://example.com/image.jpg",
        location: String = "United States of America"
    ): LaunchesUi = LaunchesUi(
        id = id,
        missionName = missionName,
        launchDate = launchDate,
        status = status,
        imageUrl = imageUrl,
        location = location
    )
}
