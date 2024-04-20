package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.seancoyle.core.ui.StringResource
import com.seancoyle.core.ui.composables.CircularProgressBar
import com.seancoyle.core.ui.composables.DisplayNotification
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LinkType
import com.seancoyle.feature.launch.api.domain.model.Links
import com.seancoyle.feature.launch.implementation.presentation.components.FilterDialog
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchBottomSheetCard
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchBottomSheetExitButton
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchesGridContent
import com.seancoyle.feature.launch.implementation.presentation.components.SwipeToRefreshComposable
import com.seancoyle.feature.launch.implementation.presentation.state.BottomSheetUiState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchViewModel
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesFilterState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesUiState
import kotlinx.coroutines.FlowPreview

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@FlowPreview
@Composable
internal fun LaunchScreenWithBottomSheet(
    uiState: LaunchesUiState,
    viewModel: LaunchViewModel,
    snackbarHostState: SnackbarHostState,
    filterState: LaunchesFilterState,
    isLandscape: Boolean,
    bottomSheetState: BottomSheetUiState,
    pullRefreshState: PullRefreshState,
    openLink: (String) -> Unit
) {
    LaunchScreen(
        uiState = uiState,
        page = viewModel.getPageState(),
        onChangeScrollPosition = viewModel::setScrollPositionState,
        loadNextPage = viewModel::nextPage,
        snackbarHostState = snackbarHostState,
        onItemClicked = viewModel::handleLaunchClick,
        onDismissNotification = viewModel::dismissError,
        getLaunchStatusIcon = viewModel::getLaunchStatusIcon,
        getCompanySummary = viewModel::buildCompanySummary,
        getLaunchDate = viewModel::getLaunchDateText
    )

    if (filterState.isVisible) {
        FilterDialog(
            currentFilterState = filterState,
            updateFilterState = viewModel::setLaunchFilterState,
            onDismiss = viewModel::setDialogFilterDisplayedState,
            newSearch = viewModel::newSearch,
            isLandScape = isLandscape
        )
    }

    if (bottomSheetState.isVisible) {
        LaunchBottomSheetScreen(
            bottomSheetUiState = bottomSheetState,
            dismiss = viewModel::dismissBottomSheet,
            openLink = openLink,
            isLandscape = isLandscape
        )
    }

    SwipeToRefreshComposable(uiState, pullRefreshState)
}

@ExperimentalMaterialApi
@FlowPreview
@Composable
internal fun LaunchScreen(
    uiState: LaunchesUiState,
    page: Int,
    onChangeScrollPosition: (Int) -> Unit,
    loadNextPage: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
    onItemClicked: (links: Links) -> Unit,
    getLaunchStatusIcon: (LaunchStatus) -> Int,
    getLaunchDate: (LaunchDateStatus) -> Int,
    getCompanySummary: (Company) -> StringResource,
    onDismissNotification: () -> Unit
) {
    when (uiState) {
        is LaunchesUiState.Success -> {
            LaunchesGridContent(
                launches = uiState.launches,
                paginationState = uiState.paginationState,
                page = page,
                onChangeScrollPosition = onChangeScrollPosition,
                loadNextPage = loadNextPage,
                onItemClicked = onItemClicked,
                getLaunchStatusIcon = getLaunchStatusIcon,
                getCompanySummary = getCompanySummary,
                getLaunchDate = getLaunchDate
            )

            uiState.notificationState?.let { notification ->
                DisplayNotification(
                    error = notification,
                    onDismiss = onDismissNotification,
                    snackbarHostState = snackbarHostState
                )
            }
        }

        is LaunchesUiState.Loading -> {
            CircularProgressBar()
        }

        is LaunchesUiState.Error -> {
            uiState.errorNotificationState?.let { error ->
                LaunchErrorScreen(
                    errorMessage = error.message,
                    retryAction = null
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
@Composable
internal fun LaunchBottomSheetScreen(
    bottomSheetUiState: BottomSheetUiState,
    dismiss: () -> Unit,
    openLink: (String) -> Unit,
    isLandscape: Boolean
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )

    if (bottomSheetUiState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = dismiss
        ) {
            BottomSheetContent(
                linkTypes = bottomSheetUiState.linkTypes,
                isLandscape = isLandscape,
                actionLinkClicked = { openLink(it) },
                dismiss = dismiss
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    linkTypes: List<LinkType>?,
    isLandscape: Boolean,
    actionLinkClicked: (String) -> Unit,
    dismiss: () -> Unit
) {
    Column {
        LaunchBottomSheetCard(
            linkTypes = linkTypes,
            isLandscape = isLandscape,
            actionLinkClicked = actionLinkClicked
        )
        LaunchBottomSheetExitButton(
            isLandscape = isLandscape,
            actionExitClicked = {
                dismiss()
            }
        )
    }
}