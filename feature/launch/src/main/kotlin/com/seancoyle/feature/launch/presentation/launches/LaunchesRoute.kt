@file:JvmName("LaunchesRouteKt")

package com.seancoyle.feature.launch.presentation.launches

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.ui.R
import com.seancoyle.core.ui.StringResource
import com.seancoyle.core.ui.components.notification.NotificationHandler
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
    snackbarHostState: SnackbarHostState,
    onNavigateToLaunch: (String, LaunchesType) -> Unit,
) {
    val state = viewModel.screenState
    val isRefreshing = state.isRefreshing
    val feedState = viewModel.feedState.collectAsLazyPagingItems()
    val notificationState by viewModel.notificationEvents.collectAsStateWithLifecycle(null)

    SideEffect {
        Timber.tag(TAG).d("LaunchRoute: feedState: $feedState")
        Timber.tag(TAG).d("LaunchRoute: screenState: ${viewModel.screenState}")
    }

    // Ensure refresh indicator is hidden after paging source recreation
    LaunchedEffect(
        feedState.loadState.refresh,
        state.query,
        state.launchStatus,
        state.launchesType
    ) {
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

    LaunchesScreen(
        feedState = feedState,
        state = state,
        isRefreshing = isRefreshing,
        columnCount = columnCount,
        selectedLaunchId = selectedLaunchId,
        onEvent = viewModel::onEvent,
        onUpdateScrollPosition = viewModel::updateScrollPosition,
        onClick = onNavigateToLaunch,
        modifier = modifier
    )

    notificationState?.let {
        NotificationHandler(
            notification = it,
            onDismissNotification = { viewModel.clearNotification() },
            snackbarHostState = snackbarHostState
        )
    }
}
