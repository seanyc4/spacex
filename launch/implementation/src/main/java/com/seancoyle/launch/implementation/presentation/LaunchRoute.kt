package com.seancoyle.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.implementation.presentation.composables.LaunchesContent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoroutinesApi
@Composable
internal fun LaunchRoute(
    modifier: Modifier = Modifier,
    viewModel: LaunchViewModel,
    refreshState: PullRefreshState,
    onCardClicked: (links: Links) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    printLogDebug("SPACEXAPP: ", "RECOMPOSING $uiState")

    LaunchScreen(
        launches = uiState.mergedLaunches,
        isLoading = viewModel.isLoading(),
        page = viewModel.getPageState(),
        modifier = modifier,
        onChangeScrollPosition = viewModel::setScrollPositionState,
        loadNextPage = viewModel::nextPage,
        pullRefreshState = refreshState,
        onCardClicked = onCardClicked
    )
}

@OptIn(ExperimentalMaterialApi::class)
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
        printLogDebug("SPACEXAPP:", "Launch.Loading")
        //LoadingLaunchCardList(itemCount = 10)
    }
}