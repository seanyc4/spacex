package com.seancoyle.launch.implementation.presentation

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.Constants.TAG
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_ui.composables.DisplayErrorAlert
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.implementation.presentation.composables.LaunchesContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
internal fun LaunchRoute(
    modifier: Modifier = Modifier,
    viewModel: LaunchViewModel,
    refreshState: PullRefreshState,
    onCardClicked: (links: Links) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchScreen(
        uiState = uiState,
        page = viewModel.getPageState(),
        modifier = modifier,
        onChangeScrollPosition = viewModel::setScrollPositionState,
        loadNextPage = viewModel::nextPage,
        pullRefreshState = refreshState,
        onCardClicked = onCardClicked,
        dismissAlert = viewModel::setErrorState
    )
}

@OptIn(ExperimentalMaterialApi::class)
@FlowPreview
@Composable
internal fun LaunchScreen(
    uiState: LaunchState,
    page: Int,
    dismissAlert: (Boolean) -> Unit,
    onChangeScrollPosition: (Int) -> Unit,
    loadNextPage: (Int) -> Unit,
    pullRefreshState: PullRefreshState,
    modifier: Modifier = Modifier,
    onCardClicked: (links: Links) -> Unit
) {
    printLogDebug(TAG, "RECOMPOSING LAUNCH SCREEN - $uiState")
    LaunchesContent(
        launches = uiState.mergedLaunches,
        isLoading = uiState.isLoading,
        page = page,
        onChangeScrollPosition = onChangeScrollPosition,
        loadNextPage = loadNextPage,
        pullRefreshState = pullRefreshState,
        modifier = modifier,
        onCardClicked = onCardClicked
    )
    if (uiState.isLoading) {
        printLogDebug(TAG, "Launch.Loading")
        CircularProgressIndicator()
    }
    uiState.errorResponse?.let { error ->
        DisplayErrorAlert(
            error = error,
            displayError = uiState.displayError,
            dismissAlert = dismissAlert
        )
    }
}