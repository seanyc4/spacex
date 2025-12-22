package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.seancoyle.core.ui.NotificationState
import com.seancoyle.feature.launch.implementation.presentation.components.FilterDialog
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchesGridContent
import com.seancoyle.feature.launch.implementation.presentation.components.SwipeToRefreshComposable
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState

@ExperimentalMaterialApi
@Composable
internal fun LaunchScreenWithBottomSheet(
    feedState: LazyPagingItems<LaunchUi>,
    screenState: LaunchesScreenState,
    notificationState: NotificationState?,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    onEvent: (LaunchEvents) -> Unit,
    isLandscape: Boolean,
) {
    LaunchScreen(
        feedState = feedState,
        screenState = screenState,
        notificationState = notificationState,
        snackbarHostState = snackbarHostState,
        onEvent = onEvent,
    )

    if (screenState.isVisible) {
        FilterDialog(
            currentFilterState = screenState,
            onEvent = onEvent,
            isLandScape = isLandscape
        )
    }

    SwipeToRefreshComposable(feedState, pullRefreshState)
}

@Composable
internal fun LaunchScreen(
    feedState: LazyPagingItems<LaunchUi>,
    screenState: LaunchesScreenState,
    notificationState: NotificationState?,
    onEvent: (LaunchEvents) -> Unit,
    snackbarHostState: SnackbarHostState,
) {

   // ReportDrawnWhen { feedState is LaunchesUiState.Success || feedState is LaunchesUiState.Error }

   //when (feedState) {

            LaunchesGridContent(
                launches = feedState,
                screenState = screenState,
                onEvent = onEvent
            )

         /*   notificationState?.let { notification ->
                DisplayNotification(
                    error = notification,
                    onDismissNotification = { onEvent(DismissNotificationEvent) },
                    snackbarHostState = snackbarHostState
                )
            }*/
        }

       /* is LaunchesUiState.Loading -> {
            CircularProgressBar()
        }

        is LaunchesUiState.Error -> {
            feedState.errorNotificationState?.let { error ->
                LaunchErrorScreen(
                    errorMessage = error.message,
                    retryAction = null
                )
            }
        }*/
