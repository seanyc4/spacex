@file:JvmName("LaunchScreenKt")

package com.seancoyle.feature.launch.presentation.launches

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.components.error.ErrorScreen
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.components.toolbar.TopAppBar
import com.seancoyle.core.ui.designsystem.pulltorefresh.RefreshableContent
import com.seancoyle.core.ui.designsystem.text.AppText
import com.seancoyle.core.ui.designsystem.theme.AppTheme
import com.seancoyle.feature.launch.presentation.launches.components.Launches
import com.seancoyle.feature.launch.presentation.launches.components.LaunchesFilterDialog
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesTab
import com.seancoyle.feature.launch.presentation.launches.model.LaunchesUi
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.launches.state.LaunchesState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesRoute(
    feedState: LazyPagingItems<LaunchesUi>,
    state: LaunchesState,
    isRefreshing: Boolean,
    windowSizeClass: WindowSizeClass,
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
    ) { innerPadding ->
        RefreshableContent(
            isRefreshing = isRefreshing,
            onRefresh = { onEvent(LaunchesEvents.PullToRefreshEvent) },
            content = {
                LaunchesContent(
                    feedState = feedState,
                    state = state,
                    windowSizeClass = windowSizeClass,
                    onEvent = onEvent,
                    onUpdateScrollPosition = onUpdateScrollPosition,
                    onClick = onClick,
                    modifier = modifier.padding(innerPadding)
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
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = AppTheme.colors.background
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        val launchesType = if (index == 0) {
                            LaunchesType.UPCOMING
                        } else {
                            LaunchesType.PAST
                        }
                        onEvent(LaunchesEvents.TabSelectedEvent(launchesType))
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
                    }
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
