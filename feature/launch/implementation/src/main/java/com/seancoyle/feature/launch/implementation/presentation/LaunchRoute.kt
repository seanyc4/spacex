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

@ExperimentalMaterialApi
@Composable
internal fun LaunchRoute(
    viewModel: LaunchViewModel,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    isLandscape: Boolean,
) {
    val uiState = viewModel.feedState.collectAsLazyPagingItems()
    val bottomSheetState by viewModel.bottomSheetState.collectAsStateWithLifecycle()
    val notificationState by viewModel.notificationState.collectAsStateWithLifecycle()
    val paginationState by viewModel.paginationState.collectAsStateWithLifecycle()

    SideEffect {
        printLogDebug("LaunchRoute", ": uiState: $uiState")
    }

    LaunchScreenWithBottomSheet(
        uiState = uiState,
        notificationState = notificationState,
        paginationState = paginationState,
        scrollState = viewModel.scrollState,
        snackbarHostState = snackbarHostState,
        filterState = viewModel.filterState,
        isLandscape = isLandscape,
        bottomSheetState = bottomSheetState,
        pullRefreshState = pullRefreshState,
        onEvent = viewModel::onEvent,
    )
}
