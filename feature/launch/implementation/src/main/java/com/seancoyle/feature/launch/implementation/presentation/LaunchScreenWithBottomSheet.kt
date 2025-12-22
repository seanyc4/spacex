package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.core.ui.composables.CircularProgressBar
import com.seancoyle.core.ui.composables.DisplayNotification
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

    LaunchesGridContent(
        launches = feedState,
        screenState = screenState,
        onEvent = onEvent
    )

    notificationState?.let { notification ->
        DisplayNotification(
            error = notification,
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

    when {
        feedState.loadState.refresh is LoadState.Loading -> {
            CircularProgressBar()
        }

        feedState.loadState.refresh is LoadState.Error -> {
            // Initial load error
        }

        feedState.loadState.append is LoadState.Loading -> {
            CircularProgressBar()
        }

    }

    SwipeToRefreshComposable(feedState, pullRefreshState)
}
