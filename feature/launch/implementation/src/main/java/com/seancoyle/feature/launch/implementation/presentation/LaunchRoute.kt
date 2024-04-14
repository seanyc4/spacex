package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.common.crashlytics.printLogDebug
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.implementation.presentation.components.FilterDialog
import com.seancoyle.feature.launch.implementation.presentation.components.SwipeToRefreshComposable
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchViewModel
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalMaterialApi::class)
@FlowPreview
@Composable
internal fun LaunchRoute(
    viewModel: LaunchViewModel,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    onItemClicked: (links: Links) -> Unit,
    windowSize: WindowSizeClass
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val launchFilterState by viewModel.filterState.collectAsStateWithLifecycle()

    SideEffect {
        printLogDebug("LaunchRoute", ": uiState: $uiState")
    }

    LaunchScreen(
        uiState = uiState,
        page = viewModel.getPageState(),
        onChangeScrollPosition = viewModel::setScrollPositionState,
        loadNextPage = viewModel::nextPage,
        snackbarHostState = snackbarHostState,
        onItemClicked = onItemClicked,
        onDismissNotification = viewModel::dismissError,
        getLaunchStatusIcon = viewModel::getLaunchStatusIcon,
        getCompanySummary = viewModel::buildCompanySummary,
        getLaunchDate = viewModel::getLaunchDateText
    )

    if (launchFilterState.isDialogFilterDisplayed) {
        FilterDialog(
            currentFilterState = launchFilterState,
            updateFilterState = viewModel::setLaunchFilterState,
            onDismiss = viewModel::setDialogFilterDisplayedState,
            newSearch = viewModel::newSearch,
            isLandScape = windowSize.heightSizeClass == WindowHeightSizeClass.Compact
        )
    }

    SwipeToRefreshComposable(uiState, pullRefreshState)
}