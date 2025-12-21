package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.seancoyle.core.common.crashlytics.printLogDebug
import timber.log.Timber

@ExperimentalMaterialApi
@Composable
internal fun LaunchRoute(
    viewModel: LaunchViewModel,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    isLandscape: Boolean,
) {
    val feedState = viewModel.feedState.collectAsLazyPagingItems()
    val bottomSheetState by viewModel.bottomSheetState.collectAsStateWithLifecycle()
    val notificationState by viewModel.notificationState.collectAsStateWithLifecycle()

    SideEffect {
        Timber.tag("LaunchViewModel").d("LaunchRoute: feedState: $feedState")
        Timber.tag("LaunchViewModel").d("LaunchRoute: screenState: ${viewModel.screenState}")
    }

    LaunchScreenWithBottomSheet(
        feedState = feedState,
        notificationState = notificationState,
        snackbarHostState = snackbarHostState,
        screenState = viewModel.screenState,
        isLandscape = isLandscape,
        bottomSheetState = bottomSheetState,
        pullRefreshState = pullRefreshState,
        onEvent = viewModel::onEvent,
    )
}
