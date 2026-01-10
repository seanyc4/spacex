@file:JvmName("LaunchScreenKt")

package com.seancoyle.feature.launch.presentation.launches

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
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
import com.seancoyle.feature.launch.presentation.launches.components.Launches
import com.seancoyle.feature.launch.presentation.launches.components.LaunchesFilterDialog
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesTab
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesScreen(
    modifier: Modifier = Modifier,
    feedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    isRefreshing: Boolean,
    columnCount: Int,
    selectedLaunchId: String?,
    onEvent: (LaunchesEvents) -> Unit,
    onUpdateScrollPosition: (Int) -> Unit,
    onClick: (String, LaunchesType) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                onClick = {
                    onEvent(LaunchesEvents.DisplayFilterDialogEvent)
                }
            )
        }, contentWindowInsets = WindowInsets.statusBars
    ) { paddingValues ->
        RefreshableContent(
            modifier = modifier,
            isRefreshing = isRefreshing,
            onRefresh = { onEvent(LaunchesEvents.PullToRefreshEvent) },
            content = {
                LaunchesContent(
                    feedState = feedState,
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
    feedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    columnCount: Int,
    selectedLaunchId: String?,
    onEvent: (LaunchesEvents) -> Unit,
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
                    Launches(
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
                 onEvent = onEvent
             )
        }
    }
}
