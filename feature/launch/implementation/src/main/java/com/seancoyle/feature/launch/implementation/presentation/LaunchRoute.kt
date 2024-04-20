package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seancoyle.core.common.crashlytics.printLogDebug
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchViewModel
import kotlinx.coroutines.FlowPreview

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@FlowPreview
@Composable
internal fun LaunchRoute(
    viewModel: LaunchViewModel,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    isLandscape: Boolean,
    openLink: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val bottomSheetState by viewModel.bottomSheetState.collectAsStateWithLifecycle()

    SideEffect {
        printLogDebug("LaunchRoute", ": uiState: $uiState")
    }

    LaunchScreenWithBottomSheet(
        uiState = uiState,
        viewModel = viewModel,
        snackbarHostState = snackbarHostState,
        filterState = filterState,
        isLandscape = isLandscape,
        bottomSheetState = bottomSheetState,
        pullRefreshState = pullRefreshState,
        openLink = openLink
    )
}