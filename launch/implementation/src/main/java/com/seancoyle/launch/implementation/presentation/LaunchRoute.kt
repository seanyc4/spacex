package com.seancoyle.launch.implementation.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.Constants.TAG
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.core_ui.composables.DisplayErrorAlert
import com.seancoyle.launch.implementation.domain.model.Links
import com.seancoyle.launch.implementation.presentation.composables.LaunchesContent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoroutinesApi
@FlowPreview
@Composable
internal fun LaunchRoute(
    viewModel: LaunchViewModel,
    refreshState: PullRefreshState,
    onItemClicked: (links: Links) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchScreen(
        uiState = uiState,
        page = viewModel.getPageState(),
        onChangeScrollPosition = viewModel::setScrollPositionState,
        loadNextPage = viewModel::nextPage,
        pullRefreshState = refreshState,
        onItemClicked = onItemClicked
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
    onItemClicked: (links: Links) -> Unit
) {
    printLogDebug(TAG, "RECOMPOSING LAUNCH SCREEN - $uiState")

    when (uiState) {
        is LaunchUiState.Success -> {
            printLogDebug(TAG, "Launch.Success")
            LaunchesContent(
                launches = uiState.launches,
                paginationState = uiState.paginationState,
                page = page,
                onChangeScrollPosition = onChangeScrollPosition,
                loadNextPage = loadNextPage,
                pullRefreshState = pullRefreshState,
                onItemClicked = onItemClicked
            )
        }

        is LaunchUiState.Loading -> {
            printLogDebug(TAG, "Launch.Loading")
            CircularProgressIndicator()
        }

        is LaunchUiState.Error -> {
            printLogDebug(TAG, "Launch.Error")
            DisplayErrorAlert(
                error = uiState.errorResponse
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        PullRefreshIndicator(
            refreshing = uiState is LaunchUiState.Error,
            state = pullRefreshState
        )
    }

}