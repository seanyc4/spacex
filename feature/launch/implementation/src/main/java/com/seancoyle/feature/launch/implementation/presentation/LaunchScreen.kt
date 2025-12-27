@file:JvmName("LaunchScreenKt")

package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.seancoyle.core.ui.components.notification.NotificationHandler
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.designsystem.pulltorefresh.RefreshableContent
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchFilterDialog
import com.seancoyle.feature.launch.implementation.presentation.components.Launches
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState
import com.seancoyle.feature.launch.implementation.presentation.state.PagingEvents
import timber.log.Timber

private const val TAG = "LaunchScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LaunchScreen(
    viewModel: LaunchViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
) {
    val feedState = viewModel.feedState.collectAsLazyPagingItems()
    val notificationState by viewModel.notificationEvents.collectAsStateWithLifecycle(null)
    val isRefreshing = viewModel.screenState.isRefreshing

    SideEffect {
        Timber.tag(TAG).d("LaunchRoute: feedState: $feedState")
        Timber.tag(TAG).d("LaunchRoute: screenState: ${viewModel.screenState}")
    }

    LaunchedEffect(feedState.loadState.refresh) {
        // reset the pull to refresh state
        val refresh = feedState.loadState.refresh
        if (refresh is LoadState.NotLoading && isRefreshing) {
            viewModel.setRefreshing(false)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.pagingEvents.collect { event ->
            when (event) {
                is PagingEvents.Refresh -> {
                    Timber.tag(TAG).d("Received PagingEvents.Refresh event")
                    feedState.refresh()
                }

                is PagingEvents.Retry -> {
                    Timber.tag(TAG).d("Received PagingEvents.Retry event")
                    feedState.retry()
                }
            }
        }
    }

    RefreshableContent(
        isRefreshing = viewModel.screenState.isRefreshing,
        onRefresh = { viewModel.onEvent(LaunchesEvents.PullToRefreshEvent) },
        content = {
            LaunchScreen(
                feedState = feedState,
                screenState = viewModel.screenState,
                windowSizeClass = windowSizeClass,
                onEvent = viewModel::onEvent,
                onUpdateScrollPosition = viewModel::updateScrollPosition
            )
        }
    )

    notificationState?.let {
        NotificationHandler(
            notification = it,
            onDismissNotification = {},
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
private fun LaunchScreen(
    feedState: LazyPagingItems<LaunchUi>,
    screenState: LaunchesScreenState,
    onEvent: (LaunchesEvents) -> Unit,
    windowSizeClass: WindowSizeClass,
    onUpdateScrollPosition: (Int) -> Unit,
) {

    when (feedState.loadState.mediator?.refresh) {
        is LoadState.Loading -> {
            CircularProgressBar()
        }

        is LoadState.Error -> {
            LaunchErrorScreen(onRetry = { onEvent(LaunchesEvents.RetryFetchEvent) })
        }

        else -> {
            Launches(
                launches = feedState,
                screenState = screenState,
                onEvent = onEvent,
                onUpdateScrollPosition = onUpdateScrollPosition
            )
        }
    }

    if (screenState.isFilterDialogVisible) {
        LaunchFilterDialog(
            currentFilterState = screenState,
            onEvent = onEvent,
            windowSizeClass = windowSizeClass
        )
    }

}
