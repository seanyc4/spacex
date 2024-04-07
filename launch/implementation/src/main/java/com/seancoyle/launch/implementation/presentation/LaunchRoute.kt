package com.seancoyle.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.util.printLogDebug
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.implementation.presentation.components.FilterDialogLandscape
import com.seancoyle.launch.implementation.presentation.components.FilterDialogPortrait
import com.seancoyle.launch.implementation.presentation.components.SwipeToRefreshComposable
import com.seancoyle.launch.implementation.presentation.state.LaunchViewModel
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
    val launchFilterState by viewModel.launchesFilterState.collectAsStateWithLifecycle()

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
        if (windowSize.heightSizeClass == WindowHeightSizeClass.Compact) {
            FilterDialogLandscape(
                filterState = launchFilterState,
                year = viewModel::setYearState,
                launchStatus = viewModel::setLaunchFilterState,
                order = viewModel::setLaunchOrderState,
                onDismiss = viewModel::setDialogFilterDisplayedState,
                newSearch = viewModel::newSearch
            )
        } else {
            FilterDialogPortrait(
                filterState = launchFilterState,
                year = viewModel::setYearState,
                launchStatus = viewModel::setLaunchFilterState,
                order = viewModel::setLaunchOrderState,
                onDismiss = viewModel::setDialogFilterDisplayedState,
                newSearch = viewModel::newSearch,
            )
        }
    }

    SwipeToRefreshComposable(uiState, pullRefreshState)
}