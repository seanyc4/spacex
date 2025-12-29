@file:JvmName("LaunchScreenKt")

package com.seancoyle.feature.launch.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.StringResource
import com.seancoyle.core.ui.components.error.ErrorScreen
import com.seancoyle.core.ui.components.notification.NotificationHandler
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.core.ui.designsystem.pulltorefresh.RefreshableContent
import com.seancoyle.feature.launch.presentation.components.LaunchFilterDialog
import com.seancoyle.feature.launch.presentation.components.Launches
import com.seancoyle.feature.launch.presentation.model.LaunchUi
import com.seancoyle.feature.launch.presentation.state.LaunchesEvents
import com.seancoyle.feature.launch.presentation.state.LaunchesScreenState
import com.seancoyle.feature.launch.presentation.state.PagingEvents
import timber.log.Timber

private const val TAG = "LaunchScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaunchScreen(
    viewModel: LaunchViewModel,
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val screenState = viewModel.screenState
    val isRefreshing = screenState.isRefreshing
    val feedState = viewModel.feedState.collectAsLazyPagingItems()
    val notificationState by viewModel.notificationEvents.collectAsStateWithLifecycle(null)

    SideEffect {
        Timber.tag(TAG).d("LaunchRoute: feedState: $feedState")
        Timber.tag(TAG).d("LaunchRoute: screenState: ${viewModel.screenState}")
    }

    // Ensure refresh indicator is hidden after paging source recreation
    LaunchedEffect(feedState.loadState.refresh, screenState.query, screenState.order, screenState.launchStatus) {
        val refresh = feedState.loadState.refresh
        if ((refresh is LoadState.NotLoading || refresh is LoadState.Error) && isRefreshing) {
            viewModel.setRefreshing(false)
        }
    }

    LaunchedEffect(feedState.loadState.append) {
        val appendError = feedState.loadState.append as? LoadState.Error
        appendError?.let {
            viewModel.emitErrorNotification(
                StringResource.ResId(R.string.network_connection_failed)
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.pagingEvents.collect { event ->
            when (event) {
                is PagingEvents.Refresh -> {
                    feedState.refresh()
                }

                is PagingEvents.Retry -> {
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
                onUpdateScrollPosition = viewModel::updateScrollPosition,
                modifier = modifier
            )
        }
    )

    notificationState?.let {
        NotificationHandler(
            notification = it,
            onDismissNotification = { viewModel.clearNotification() },
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
    modifier: Modifier = Modifier,
) {

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
                screenState = screenState,
                onEvent = onEvent,
                onUpdateScrollPosition = onUpdateScrollPosition,
                modifier = modifier
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
