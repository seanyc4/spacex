@file:JvmName("LaunchScreenKt")

package com.seancoyle.feature.launch.presentation.launches

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.animation.ContentStateTransitions.slowFadeTransition
import com.seancoyle.core.ui.components.error.ErrorState
import com.seancoyle.core.ui.components.toolbar.TopAppBar
import com.seancoyle.core.ui.designsystem.pulltorefresh.RefreshableContent
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launches.components.Launches
import com.seancoyle.feature.launch.presentation.launches.components.LaunchesLoadingState
import com.seancoyle.feature.launch.presentation.launches.filter.FilterBottomSheetRoute
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesTab
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesScreen(
    modifier: Modifier = Modifier,
    upcomingFeedState: LazyPagingItems<LaunchesUi>,
    pastFeedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    isRefreshing: Boolean,
    columnCount: Int,
    selectedLaunchId: String?,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (LaunchesType, Int) -> Unit,
    onClick: (String, LaunchesType) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onClick = {
                    onEvent(LaunchesEvents.DisplayFilterBottomSheetEvent)
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        RefreshableContent(
            modifier = modifier,
            isRefreshing = isRefreshing,
            onRefresh = { onEvent(LaunchesEvents.PullToRefreshEvent) },
            content = {
                LaunchesContent(
                    upcomingFeedState = upcomingFeedState,
                    pastFeedState = pastFeedState,
                    state = state,
                    columnCount = columnCount,
                    selectedLaunchId = selectedLaunchId,
                    onEvent = onEvent,
                    onUpdateScrollPosition = onUpdateScrollPosition,
                    onClick = onClick,
                    modifier = modifier.padding(paddingValues)
                )
            }
        )
    }
}

@Composable
private fun LaunchesContent(
    upcomingFeedState: LazyPagingItems<LaunchesUi>,
    pastFeedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    columnCount: Int,
    selectedLaunchId: String?,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (LaunchesType, Int) -> Unit,
    onClick: (String, LaunchesType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = LaunchesTab.provideTabs()
    val pagerState = rememberPagerState(
        initialPage = when (state.launchesType) {
            LaunchesType.UPCOMING -> 0
            LaunchesType.PAST -> 1
        }
    ) {
        tabs.size
    }

    // Sync pager state with external state changes
    LaunchedEffect(state.launchesType) {
        val targetPage = when (state.launchesType) {
            LaunchesType.UPCOMING -> 0
            LaunchesType.PAST -> 1
        }
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    // Sync external state with pager gestures
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            val launchType = when (pagerState.currentPage) {
                0 -> LaunchesType.UPCOMING
                1 -> LaunchesType.PAST
                else -> LaunchesType.UPCOMING
            }
            if (state.launchesType != launchType) {
                onEvent(LaunchesEvents.TabSelectedEvent(launchType))
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        SecondaryTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = AppTheme.colors.background
        ) {
            tabs.forEachIndexed { index, tab ->
                val testTag: String
                val contentDesc: String
                val launchType: LaunchesType

                if (index == 0) {
                    testTag = LaunchesTestTags.UPCOMING_TAB
                    contentDesc = stringResource(R.string.upcoming_tab_desc)
                    launchType = LaunchesType.UPCOMING
                } else {
                    testTag = LaunchesTestTags.PAST_TAB
                    contentDesc = stringResource(R.string.past_tab_desc)
                    launchType = LaunchesType.PAST
                }
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        onEvent(LaunchesEvents.TabSelectedEvent(launchType))
                    },
                    text = {
                        AppText.titleSmall(
                            text = stringResource(tab.title),
                            color = if (pagerState.currentPage == index) {
                                AppTheme.colors.onSurface
                            } else {
                                AppTheme.colors.onSurface.copy(alpha = 0.3f)
                            }
                        )
                    },
                    modifier = Modifier
                        .testTag(testTag)
                        .semantics { contentDescription = contentDesc }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                LaunchesType.UPCOMING.ordinal -> LaunchesListContent(
                    feedState = upcomingFeedState,
                    state = state,
                    launchesType = LaunchesType.UPCOMING,
                    columnCount = columnCount,
                    selectedLaunchId = selectedLaunchId,
                    onEvent = onEvent,
                    onUpdateScrollPosition = onUpdateScrollPosition,
                    onClick = onClick
                )
                LaunchesType.PAST.ordinal -> LaunchesListContent(
                    feedState = pastFeedState,
                    state = state,
                    launchesType = LaunchesType.PAST,
                    columnCount = columnCount,
                    selectedLaunchId = selectedLaunchId,
                    onEvent = onEvent,
                    onUpdateScrollPosition = onUpdateScrollPosition,
                    onClick = onClick
                )
            }
        }

        // Filter Bottom Sheet
        if (state.isFilterBottomSheetVisible) {
            FilterBottomSheetRoute(
                currentQuery = state.query,
                currentStatus = state.launchStatus,
                onApplyFilters = { result ->
                    onEvent(
                        LaunchesEvents.UpdateFilterStateEvent(
                            query = result.query,
                            launchStatus = result.status
                        )
                    )
                },
                onDismiss = {
                    onEvent(LaunchesEvents.DismissFilterBottomSheetEvent)
                }
            )
        }
    }
}

@Composable
private fun LaunchesListContent(
    feedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    launchesType: LaunchesType,
    columnCount: Int,
    selectedLaunchId: String?,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (LaunchesType, Int) -> Unit,
    onClick: (String, LaunchesType) -> Unit
) {
    val refreshLoadState = feedState.loadState.refresh
    val endOfPaginationReached = feedState.loadState.append.endOfPaginationReached

    AnimatedContent(
        targetState = refreshLoadState,
        transitionSpec = { slowFadeTransition() },
        label = "LaunchesContentAnimation"
    ) { loadState ->
        when (loadState) {
            is LoadState.Loading -> {
                LaunchesLoadingState(columnCount = columnCount)
            }

            is LoadState.Error -> {
                ErrorState(
                    message = stringResource(R.string.unable_to_load),
                    modifier = Modifier.fillMaxSize(),
                    showRetryButton = true,
                    onRetry = { onEvent(LaunchesEvents.RetryFetchEvent) }
                )
            }

            is LoadState.NotLoading -> {
                if (feedState.itemCount == 0 && endOfPaginationReached) {
                    ErrorState(
                        message = stringResource(R.string.empty_data),
                        modifier = Modifier.fillMaxSize(),
                        showRetryButton = false,
                        onRetry = { }
                    )
                } else {
                    Launches(
                        launches = feedState,
                        state = state,
                        launchesType = launchesType,
                        columnCount = columnCount,
                        selectedLaunchId = selectedLaunchId,
                        onEvent = onEvent,
                        onUpdateScrollPosition = { position ->
                            onUpdateScrollPosition(launchesType, position)
                        },
                        onClick = onClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
