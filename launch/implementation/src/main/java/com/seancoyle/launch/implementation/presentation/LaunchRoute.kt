package com.seancoyle.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_ui.composables.CircularProgressBar
import com.seancoyle.core_ui.composables.DisplayErrorAlert
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchSuccessStatus
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.implementation.presentation.composables.LaunchesContent
import com.seancoyle.launch.implementation.presentation.composables.SwipeToRefreshComposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
internal fun LaunchRoute(
    viewModel: LaunchViewModel,
    refreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    onItemClicked: (links: Links) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SideEffect {
        printLogDebug("LaunchRoute", ": uiState: $uiState")
    }

    LaunchScreen(
        uiState = uiState,
        page = viewModel.getPageState(),
        onChangeScrollPosition = viewModel::setScrollPositionState,
        loadNextPage = viewModel::nextPage,
        pullRefreshState = refreshState,
        snackbarHostState = snackbarHostState,
        onItemClicked = onItemClicked,
        onDismissError = viewModel::dismissError,
        getLaunchStatusIcon = viewModel::getLaunchStatusIcon,
        getLaunchDate = viewModel::getLaunchDateText
    )
}

@OptIn(ExperimentalMaterialApi::class)
@FlowPreview
@Composable
internal fun LaunchScreen(
    uiState: LaunchUiState,
    page: Int,
    onChangeScrollPosition: (Int) -> Unit,
    loadNextPage: (Int) -> Unit,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    onItemClicked: (links: Links) -> Unit,
    getLaunchStatusIcon: (LaunchSuccessStatus) -> Int,
    getLaunchDate: (LaunchDateStatus) -> Int,
    onDismissError: () -> Unit
) {
    when (uiState) {
        is LaunchUiState.Success -> {
            LaunchesContent(
                launches = uiState.launches,
                paginationState = uiState.paginationState,
                page = page,
                onChangeScrollPosition = onChangeScrollPosition,
                loadNextPage = loadNextPage,
                pullRefreshState = pullRefreshState,
                onItemClicked = onItemClicked,
                getLaunchStatusIcon = getLaunchStatusIcon,
                getLaunchDate = getLaunchDate
            )
        }

        is LaunchUiState.Loading -> {
            CircularProgressBar()
        }

        is LaunchUiState.Error -> {
            DisplayErrorAlert(
                error = uiState.errorResponse,
                onDismiss = onDismissError,
                snackbarHostState = snackbarHostState
            )
        }
    }
    SwipeToRefreshComposable(uiState, pullRefreshState)
}
