package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.components.Launches
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class LaunchesScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun launchesList_hasCorrectTestTag() {
        val launches = createTestLaunches(count = 3)
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_LAZY_COLUMN)
            .assertIsDisplayed()
    }

    @Test
    fun launchesList_displaysMultipleLaunchCards() {
        val launches = listOf(
            createTestLaunchesUi(id = "1", missionName = "Mission Alpha"),
            createTestLaunchesUi(id = "2", missionName = "Mission Beta"),
            createTestLaunchesUi(id = "3", missionName = "Mission Gamma")
        )
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        // First item should be visible
        composeRule.onNodeWithText("Mission Alpha")
            .assertIsDisplayed()
    }

    @Test
    fun launchesList_scrollsToRevealLaterItems() {
        val launches = createTestLaunches(count = 60)
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        // Scroll to the last item
        composeRule.onNode(hasScrollAction())
            .performScrollToNode(hasText("Mission 50"))

        // A card should still be visible after scrolling
        composeRule.onNodeWithText("Mission 51")
            .assertExists()
    }

    @Test
    fun launchesList_invokesClickCallback() {
        var clickedId: String? = null
        var clickedType: LaunchesType? = null
        val testLaunch = createTestLaunchesUi(id = "test-id-123")
        val pagingData = PagingData.from(listOf(testLaunch))

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(launchesType = LaunchesType.UPCOMING),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { id, type ->
                            clickedId = id
                            clickedType = type
                        }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .performClick()

        assert(clickedId == "test-id-123") { "Expected id test-id-123 but got $clickedId" }
        assert(clickedType == LaunchesType.UPCOMING) { "Expected UPCOMING but got $clickedType" }
    }

    // ==================== EMPTY STATE TESTS ====================

    @Test
    fun launchesList_emptyState_displaysLazyColumn() {
        val pagingData = PagingData.from(emptyList<LaunchesUi>())

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        // LazyColumn should still exist even when empty
        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_LAZY_COLUMN)
            .assertIsDisplayed()

        // But no cards should be present
        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .assertDoesNotExist()
    }

    // ==================== STATE-DRIVEN UI TESTS ====================

    @Test
    fun launchesList_respectsScrollPositionFromState() {
        val launches = createTestLaunches(count = 10)
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(scrollPosition = 0),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        // First item should be visible at initial position
        composeRule.onNodeWithText("Mission 0")
            .assertIsDisplayed()
    }

    @Test
    fun launchesList_invokesScrollPositionCallback() {
        var scrollPosition = -1
        val launches = createTestLaunches(count = 20)
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        onEvent = {},
                        onUpdateScrollPosition = { pos -> scrollPosition = pos },
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        composeRule.onNode(hasScrollAction())
            .performScrollToNode(hasText("Mission 15"))

        // Initial scroll position should be reported after 750ms
        composeRule.waitUntil(1000L) {
            scrollPosition >0
        }

        assert(scrollPosition >= 0) { "Expected scroll position to be tracked" }
    }

    @Test
    fun launchesList_passesUpcomingTypeToCallback() {
        var clickedType: LaunchesType? = null
        val testLaunch = createTestLaunchesUi()
        val pagingData = PagingData.from(listOf(testLaunch))

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(launchesType = LaunchesType.UPCOMING),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, type -> clickedType = type }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .performClick()

        assert(clickedType == LaunchesType.UPCOMING)
    }

    @Test
    fun launchesList_passesPastTypeToCallback() {
        var clickedType: LaunchesType? = null
        val testLaunch = createTestLaunchesUi()
        val pagingData = PagingData.from(listOf(testLaunch))

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(launchesType = LaunchesType.PAST),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, type -> clickedType = type }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCH_CARD)
            .performClick()

        assert(clickedType == LaunchesType.PAST)
    }

    @Test
    fun launchesList_displaysCardsWithDifferentStatuses() {
        val launches = listOf(
            createTestLaunchesUi(id = "1", missionName = "GO Mission", status = LaunchStatus.GO),
            createTestLaunchesUi(id = "2", missionName = "TBD Mission", status = LaunchStatus.TBD),
            createTestLaunchesUi(id = "3", missionName = "Success Mission", status = LaunchStatus.SUCCESS)
        )
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    Launches(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        composeRule.onNodeWithText("GO Mission")
            .assertIsDisplayed()
        composeRule.onNodeWithText("TBD Mission")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Success Mission")
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
        imageUrl: String = "https://example.com/image.jpg"
    ): LaunchesUi = LaunchesUi(
        id = id,
        missionName = missionName,
        launchDate = launchDate,
        status = status,
        imageUrl = imageUrl
    )
}
