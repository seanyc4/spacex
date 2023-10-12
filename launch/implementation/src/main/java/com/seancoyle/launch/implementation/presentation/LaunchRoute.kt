package com.seancoyle.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.Constants.TAG
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.ViewType
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
        launches = uiState.mergedLaunches,
        isLoading = uiState.isLoading,
        page = uiState.page,
        modifier = modifier,
        onChangeScrollPosition = viewModel::setScrollPositionState,
        loadNextPage = viewModel::nextPage,
        pullRefreshState = refreshState,
        onCardClicked = onCardClicked
    )
}

@OptIn(ExperimentalMaterialApi::class)
@FlowPreview
@Composable
internal fun LaunchScreen(
    launches: List<ViewType>,
    isLoading: Boolean,
    page: Int,
    onChangeScrollPosition: (Int) -> Unit,
    loadNextPage: (Int) -> Unit,
    pullRefreshState: PullRefreshState,
    modifier: Modifier = Modifier,
    onCardClicked: (links: Links) -> Unit
) {
    printLogDebug(TAG, "RECOMPOSING LAUNCH SCREEN")
    LaunchesContent(
        launches = launches,
        isLoading = isLoading,
        page = page,
        onChangeScrollPosition = onChangeScrollPosition,
        loadNextPage = loadNextPage,
        pullRefreshState = pullRefreshState,
        modifier = modifier,
        onCardClicked = onCardClicked
    )
    if (isLoading) {
        printLogDebug(TAG, "Launch.Loading")
        //LoadingLaunchCardList(itemCount = 10)
    }
}