@file:JvmName("LaunchesRouteKt")

package com.seancoyle.feature.launch.presentation.launches

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.presentation.launches.state.PagingEvents
import timber.log.Timber

private const val TAG = "LaunchesRoute"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchesRoute(
    modifier: Modifier = Modifier,
    viewModel: LaunchesViewModel = hiltViewModel(),
    columnCount: Int = 1,
    selectedLaunchId: String? = null,
    onNavigateToLaunch: (String, LaunchesType) -> Unit,
) {
    val state = viewModel.screenState
    val isRefreshing = state.isRefreshing
    val upcomingLaunches = viewModel.upcomingLaunchesFlow.collectAsLazyPagingItems()
    val pastLaunches = viewModel.pastLaunchesFlow.collectAsLazyPagingItems()

    SideEffect {
        Timber.tag(TAG).d("LaunchRoute: currentTab=${state.launchesType}")
        Timber.tag(TAG).d("LaunchRoute: upcomingItemCount=${upcomingLaunches.itemCount}, pastItemCount=${pastLaunches.itemCount}")
    }

    LaunchedEffect(
        upcomingLaunches.loadState.refresh,
        state.query,
        state.launchesType,
        state.launchStatus
    ) {
        val refresh = upcomingLaunches.loadState.refresh
        if ((refresh is LoadState.NotLoading || refresh is LoadState.Error) &&
            isRefreshing && state.launchesType == LaunchesType.UPCOMING
        ) {
            viewModel.setRefreshing(false)
        }
    }

    LaunchedEffect(
        pastLaunches.loadState.refresh,
        state.query,
        state.launchStatus,
        state.launchesType
    ) {
        val refresh = pastLaunches.loadState.refresh
        if ((refresh is LoadState.NotLoading || refresh is LoadState.Error) &&
            isRefreshing && state.launchesType == LaunchesType.PAST
        ) {
            viewModel.setRefreshing(false)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.upcomingPagingEvents.collect { event ->
            when (event) {
                is PagingEvents.Refresh -> upcomingLaunches.refresh()
                is PagingEvents.Retry -> upcomingLaunches.retry()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.pastPagingEvents.collect { event ->
            when (event) {
                is PagingEvents.Refresh -> pastLaunches.refresh()
                is PagingEvents.Retry -> pastLaunches.retry()
            }
        }
    }

    LaunchesScreen(
        upcomingFeedState = upcomingLaunches,
        pastFeedState = pastLaunches,
        state = state,
        isRefreshing = isRefreshing,
        columnCount = columnCount,
        selectedLaunchId = selectedLaunchId,
        onEvent = viewModel::onEvent,
        onUpdateScrollPosition = viewModel::updateScrollPosition,
        onClick = onNavigateToLaunch,
        modifier = modifier
    )
}
