package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.components.notification.NotificationHandler
import com.seancoyle.core.ui.components.progress.CircularProgressBar
import com.seancoyle.feature.launch.implementation.presentation.components.FilterDialog
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchesGridContent
import com.seancoyle.feature.launch.implementation.presentation.components.SwipeToRefreshComposable
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState

@ExperimentalMaterialApi
@Composable
internal fun LaunchScreenWithBottomSheet(
    feedState: LazyPagingItems<LaunchUi>,
    screenState: LaunchesScreenState,
    notificationState: NotificationState?,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    onEvent: (LaunchesEvents) -> Unit,
    isLandscape: Boolean,
) {

    when {
        feedState.loadState.refresh is LoadState.Loading -> {
            CircularProgressBar()
        }
        feedState.loadState.refresh is LoadState.Error -> {
            // Initial load error
        }
        feedState.loadState.prepend is LoadState.Loading -> {
            CircularProgressBar()
        }
        feedState.loadState.prepend is LoadState.Error -> {

        }
        else -> {
            LaunchesGridContent(
                launches = feedState,
                screenState = screenState,
                onEvent = onEvent
            )
        }
    }

    notificationState?.let { notification ->
        NotificationHandler(
            notification = notification,
            onDismissNotification = { onEvent(LaunchesEvents.DismissNotificationEvent) },
            snackbarHostState = snackbarHostState
        )
    }

    if (screenState.isVisible) {
        FilterDialog(
            currentFilterState = screenState,
            onEvent = onEvent,
            isLandScape = isLandscape
        )
    }

    SwipeToRefreshComposable(feedState, pullRefreshState)
}
