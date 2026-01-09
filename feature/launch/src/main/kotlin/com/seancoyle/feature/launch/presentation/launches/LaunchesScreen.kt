@file:JvmName("LaunchScreenKt")

package com.seancoyle.feature.launch.presentation.launches

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.test.testags.LaunchesTestTags
import com.seancoyle.core.ui.components.error.ErrorScreen
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.components.toolbar.TopAppBar
import com.seancoyle.core.ui.designsystem.pulltorefresh.RefreshableContent
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.R
import com.seancoyle.feature.launch.presentation.launches.components.AdaptiveLaunchesGrid
import com.seancoyle.feature.launch.presentation.launches.components.LaunchesFilterDialog
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesTab
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesScreen(
    feedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    isRefreshing: Boolean,
    windowSizeClass: WindowSizeClass,
    columnCount: Int = 1,
    selectedLaunchId: String? = null,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (Int) -> Unit,
    onClick: (String, LaunchesType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onClick = {
                    onEvent(LaunchesEvents.DisplayFilterDialogEvent)
                }
            )
        },
    ) { paddingValues ->
        RefreshableContent(
            modifier = modifier.padding(paddingValues),
            isRefreshing = isRefreshing,
            onRefresh = { onEvent(LaunchesEvents.PullToRefreshEvent) },
            content = {
                LaunchesContent(
                    feedState = feedState,
                    state = state,
                    windowSizeClass = windowSizeClass,
                    columnCount = columnCount,
                    selectedLaunchId = selectedLaunchId,
                    onEvent = onEvent,
                    onUpdateScrollPosition = onUpdateScrollPosition,
                    onClick = onClick,
                    modifier = modifier
                )
            }
        )
    }
}

@Composable
private fun LaunchesContent(
    feedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    onEvent: (LaunchesEvents) -> Unit,
    windowSizeClass: WindowSizeClass,
    columnCount: Int,
    selectedLaunchId: String?,
    onUpdateScrollPosition: (Int) -> Unit,
    onClick: (String, LaunchesType) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabs = LaunchesTab.provideTabs()
    val selectedTabIndex = when (state.launchesType) {
        LaunchesType.UPCOMING -> 0
        LaunchesType.PAST -> 1
    }
    Column(modifier = modifier.fillMaxSize()) {
        SecondaryTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = AppTheme.colors.background
        ) {
            tabs.forEachIndexed { index, tab ->
                var testTag: String
                var contentDesc: String
                var launchType: LaunchesType

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
                    selected = selectedTabIndex == index,
                    onClick = {
                        onEvent(LaunchesEvents.TabSelectedEvent(launchType))
                    },
                    text = {
                        AppText.titleSmall(
                            text = stringResource(tab.title),
                            color = if (selectedTabIndex == index) {
                                AppTheme.colors.onSurface
                            } else {
                                AppTheme.colors.onSurface.copy(alpha = 0.6f)
                            }
                        )
                    },
                    modifier = Modifier
                        .testTag(testTag)
                        .semantics { contentDescription = contentDesc }
                )
            }
        }
        Box(modifier = Modifier.weight(1f)) {
            when (feedState.loadState.mediator?.refresh) {
                is LoadState.Loading -> {
                    CircularProgressBar()
                }

                is LoadState.Error -> {
                    ErrorScreen(onRetry = { onEvent(LaunchesEvents.RetryFetchEvent) })
                }

                else -> {
                    AdaptiveLaunchesGrid(
                        launches = feedState,
                        state = state,
                        columnCount = columnCount,
                        selectedLaunchId = selectedLaunchId,
                        onEvent = onEvent,
                        onUpdateScrollPosition = onUpdateScrollPosition,
                        onClick = onClick,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        if (state.isFilterDialogVisible) {
            LaunchesFilterDialog(
                currentFilterState = state,
                onEvent = onEvent,
                windowSizeClass = windowSizeClass
            )
        }
    }
}
