package com.seancoyle.feature.launch.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.ui.Alignment
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
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.adaptive.AdaptiveLayoutTestTags
import com.seancoyle.core.ui.adaptive.DetailPlaceholder
import com.seancoyle.core.ui.adaptive.TwoPaneLayout
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launch.model.LaunchStatus
import com.seancoyle.feature.launch.presentation.launches.components.AdaptiveLaunchesGrid
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class AdaptiveLayoutTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun twoPaneLayout_displaysListPane() {
        composeRule.setContent {
            AppTheme {
                TwoPaneLayout(
                    listPane = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("List Pane Content")
                        }
                    },
                    detailPane = {
                        Text("Detail Pane Content")
                    },
                    showDetailPane = false
                )
            }
        }

        composeRule.onNodeWithTag(AdaptiveLayoutTestTags.TWO_PANE_LAYOUT)
            .assertIsDisplayed()
        composeRule.onNodeWithTag(AdaptiveLayoutTestTags.LIST_PANE)
            .assertIsDisplayed()
        composeRule.onNodeWithText("List Pane Content")
            .assertIsDisplayed()
    }

    @Test
    fun twoPaneLayout_displaysDetailPaneWhenSelected() {
        composeRule.setContent {
            AppTheme {
                TwoPaneLayout(
                    listPane = {
                        Text("List Pane Content")
                    },
                    detailPane = {
                        Text("Detail Pane Content")
                    },
                    showDetailPane = true
                )
            }
        }

        composeRule.onNodeWithTag(AdaptiveLayoutTestTags.DETAIL_PANE)
            .assertIsDisplayed()
        composeRule.onNodeWithText("Detail Pane Content")
            .assertIsDisplayed()
    }

    @Test
    fun twoPaneLayout_displaysPlaceholderWhenNoSelection() {
        composeRule.setContent {
            AppTheme {
                TwoPaneLayout(
                    listPane = {
                        Text("List Pane Content")
                    },
                    detailPane = {
                        Text("Detail Pane Content")
                    },
                    showDetailPane = false,
                    placeholder = {
                        DetailPlaceholder()
                    }
                )
            }
        }

        composeRule.onNodeWithTag(AdaptiveLayoutTestTags.PLACEHOLDER_PANE)
            .assertIsDisplayed()
        composeRule.onNodeWithText("Select an item to view details")
            .assertIsDisplayed()
    }

    @Test
    fun twoPaneLayout_displaysDivider() {
        composeRule.setContent {
            AppTheme {
                TwoPaneLayout(
                    listPane = { Text("List") },
                    detailPane = { Text("Detail") },
                    showDetailPane = false
                )
            }
        }

        composeRule.onNodeWithTag(AdaptiveLayoutTestTags.PANE_DIVIDER)
            .assertIsDisplayed()
    }

    @Test
    fun detailPlaceholder_displaysMessage() {
        val message = "Select an item to view details"

        composeRule.setContent {
            AppTheme {
                DetailPlaceholder()
            }
        }

        composeRule.onNodeWithText(message)
            .assertIsDisplayed()
    }

    @Test
    fun detailPlaceholder_hasCorrectTestTag() {
        composeRule.setContent {
            AppTheme {
                DetailPlaceholder()
            }
        }

        composeRule.onNodeWithTag(AdaptiveLayoutTestTags.PLACEHOLDER_PANE)
            .assertIsDisplayed()
    }

    @Test
    fun adaptiveLaunchesGrid_displaysSingleColumn() {
        val launches = createTestLaunches(count = 5)
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    AdaptiveLaunchesGrid(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        columnCount = 1,
                        selectedLaunchId = null,
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCHES_SCREEN)
            .assertIsDisplayed()
        composeRule.onNodeWithText("Mission 1")
            .assertIsDisplayed()
    }

    @Test
    fun adaptiveLaunchesGrid_displaysTwoColumns() {
        val launches = createTestLaunches(count = 6)
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    AdaptiveLaunchesGrid(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        columnCount = 2,
                        selectedLaunchId = null,
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        composeRule.onNodeWithTag(LaunchesTestTags.LAUNCHES_SCREEN)
            .assertIsDisplayed()
        // First item should be visible
        composeRule.onNodeWithText("Mission 1")
            .assertIsDisplayed()
    }

    @Test
    fun adaptiveLaunchesGrid_highlightsSelectedItem() {
        val launches = createTestLaunches(count = 3)
        val pagingData = PagingData.from(launches)
        val selectedId = "2"

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    AdaptiveLaunchesGrid(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        columnCount = 1,
                        selectedLaunchId = selectedId,
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        // The selected card should be displayed
        composeRule.onNodeWithText("Mission 2")
            .assertIsDisplayed()
    }

    @Test
    fun adaptiveLaunchesGrid_invokesClickCallback() {
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

                    AdaptiveLaunchesGrid(
                        launches = lazyPagingItems,
                        state = LaunchesState(launchesType = LaunchesType.UPCOMING),
                        columnCount = 1,
                        selectedLaunchId = null,
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

        assert(clickedId == "test-id-123") { "Expected clickedId to be 'test-id-123', but was $clickedId" }
        assert(clickedType == LaunchesType.UPCOMING) { "Expected clickedType to be UPCOMING, but was $clickedType" }
    }

    @Test
    fun adaptiveLaunchesGrid_scrollsToRevealLaterItems() {
        val launches = createTestLaunches(count = 30)
        val pagingData = PagingData.from(launches)

        composeRule.setContent {
            AppTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(AppTheme.colors.background)
                ) {
                    val lazyPagingItems = flowOf(pagingData).collectAsLazyPagingItems()

                    AdaptiveLaunchesGrid(
                        launches = lazyPagingItems,
                        state = LaunchesState(),
                        columnCount = 1,
                        selectedLaunchId = null,
                        onEvent = {},
                        onUpdateScrollPosition = {},
                        onClick = { _, _ -> }
                    )
                }
            }
        }

        // Scroll to a later item
        composeRule.onNode(hasScrollAction())
            .performScrollToNode(hasText("Mission 20"))

        composeRule.onNodeWithText("Mission 20")
            .assertExists()
    }

    private fun createTestLaunches(count: Int): List<LaunchesUi> {
        return (1..count).map { index ->
            createTestLaunchesUi(
                id = "$index",
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
