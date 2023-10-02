package com.seancoyle.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.presentation.LaunchUiState
import com.seancoyle.launch.implementation.presentation.composables.LaunchesContent
import com.seancoyle.launch.implementation.presentation.composables.LoadingLaunchCardList
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
    printLogDebug("SPACEXAPP: RECOMPOSING", "RECOMPOSING $uiState")
    LaunchScreen(
        uiState = uiState,
        modifier = modifier,
        onChangeScrollPosition = viewModel::setScrollPositionState,
        loadNextPage = viewModel::nextPage,
        page = viewModel.getPageState(),
        pullRefreshState = refreshState,
        onCardClicked = onCardClicked
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LaunchScreen(
    uiState: LaunchUiState,
    onChangeScrollPosition: (Int) -> Unit,
    page: Int,
    loadNextPage: () -> Unit,
    pullRefreshState: PullRefreshState,
    modifier: Modifier = Modifier,
    onCardClicked: (links: Links) -> Unit
) {

    when (uiState) {
        is LaunchUiState.Loading -> {
            printLogDebug("SPACEXAPP:", "LaunchUiState.Loading")
            LoadingLaunchCardList(itemCount = 10)
        }

        is LaunchUiState.LaunchState -> {
            printLogDebug("SPACEXAPP:", "LaunchUiState.LaunchState")
            LaunchesContent(
                launches = uiState.mergedLaunches,
                onChangeScrollPosition = onChangeScrollPosition,
                page = page,
                loadNextPage = loadNextPage,
                pullRefreshState = pullRefreshState,
                modifier = modifier,
                onCardClicked = onCardClicked
            )
        }

        is LaunchUiState.Empty -> {

    }
        is LaunchUiState.ErrorState -> {

        }
    }
}