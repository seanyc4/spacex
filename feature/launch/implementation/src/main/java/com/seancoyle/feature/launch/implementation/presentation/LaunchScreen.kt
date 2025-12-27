@file:JvmName("LaunchScreenKt")

package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
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
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchFilterDialog
import com.seancoyle.feature.launch.implementation.presentation.components.Launches
import com.seancoyle.feature.launch.implementation.presentation.components.PullToRefreshIndicator
import com.seancoyle.feature.launch.implementation.presentation.model.LaunchUi
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScreenState
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@ExperimentalMaterialApi
@Composable
internal fun LaunchScreen(
    viewModel: LaunchViewModel = hiltViewModel(),
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    windowSizeClass: WindowSizeClass,
) {
    val feedState = viewModel.feedState.collectAsLazyPagingItems()
    val notificationState by viewModel.notificationEvents.collectAsStateWithLifecycle(null)

    SideEffect {
        Timber.tag("LaunchViewModel").d("LaunchRoute: feedState: $feedState")
        Timber.tag("LaunchViewModel").d("LaunchRoute: screenState: ${viewModel.screenState}")
    }

    LaunchScreen(
        feedState = feedState,
        screenState = viewModel.screenState,
        windowSizeClass = windowSizeClass,
        pullRefreshState = pullRefreshState,
        onEvent = viewModel::onEvent,
    )

    LaunchedEffect(Unit) {
        viewModel.refreshEvent.collectLatest {
            feedState.refresh()
        }
    }

    notificationState?.let {
        NotificationHandler(
            notification = it,
            onDismissNotification = {},
            snackbarHostState = snackbarHostState
        )
    }
}

@ExperimentalMaterialApi
@Composable
private fun LaunchScreen(
    feedState: LazyPagingItems<LaunchUi>,
    screenState: LaunchesScreenState,
    pullRefreshState: PullRefreshState,
    onEvent: (LaunchesEvents) -> Unit,
    windowSizeClass: WindowSizeClass,
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
            Launches(
                launches = feedState,
                screenState = screenState,
                onEvent = onEvent
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

    PullToRefreshIndicator(feedState, pullRefreshState)
}
