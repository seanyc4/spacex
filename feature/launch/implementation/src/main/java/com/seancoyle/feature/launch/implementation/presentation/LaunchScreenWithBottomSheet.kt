package com.seancoyle.feature.launch.implementation.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.seancoyle.core.ui.composables.CircularProgressBar
import com.seancoyle.core.ui.composables.DisplayNotification
import com.seancoyle.core.ui.extensions.adaptiveHorizontalPadding
import com.seancoyle.feature.launch.api.domain.model.LinkType
import com.seancoyle.feature.launch.implementation.presentation.components.FilterDialog
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchBottomSheetCard
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchBottomSheetExitButton
import com.seancoyle.feature.launch.implementation.presentation.components.LaunchesGridContent
import com.seancoyle.feature.launch.implementation.presentation.components.SwipeToRefreshComposable
import com.seancoyle.feature.launch.implementation.presentation.state.BottomSheetUiState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchEvents
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesFilterState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesScrollState
import com.seancoyle.feature.launch.implementation.presentation.state.LaunchesUiState
import kotlinx.coroutines.FlowPreview

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@FlowPreview
@Composable
internal fun LaunchScreenWithBottomSheet(
    uiState: LaunchesUiState,
    filterState: LaunchesFilterState,
    scrollState: LaunchesScrollState,
    bottomSheetState: BottomSheetUiState,
    pullRefreshState: PullRefreshState,
    snackbarHostState: SnackbarHostState,
    onEvent: (LaunchEvents) -> Unit,
    onDismissNotification: () -> Unit,
    isLandscape: Boolean,
) {
    LaunchScreen(
        uiState = uiState,
        onEvent = onEvent,
        scrollState = scrollState,
        snackbarHostState = snackbarHostState,
        onDismissNotification = onDismissNotification
    )

    if (filterState.isVisible) {
        FilterDialog(
            currentFilterState = filterState,
            onEvent = onEvent,
            isLandScape = isLandscape
        )
    }

    if (bottomSheetState.isVisible) {
        LaunchBottomSheetScreen(
            bottomSheetUiState = bottomSheetState,
            onEvent = onEvent,
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
    scrollState: LaunchesScrollState,
    onEvent: (LaunchEvents) -> Unit,
    onDismissNotification: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    when (uiState) {
        is LaunchesUiState.Success -> {
            LaunchesGridContent(
                launches = uiState.launches,
                paginationState = uiState.paginationState,
                scrollState = scrollState,
                onEvent = onEvent
            )

            uiState.notificationState?.let { notification ->
                DisplayNotification(
                    error = notification,
                    onDismissNotification = onDismissNotification,
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
    onEvent: (LaunchEvents) -> Unit,
    isLandscape: Boolean
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { true }
    )

    if (bottomSheetUiState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { onEvent(LaunchEvents.DismissBottomSheetEvent) },
            modifier = Modifier.adaptiveHorizontalPadding(isLandscape = isLandscape, horizontalPadding = 164.dp)
        ) {
            BottomSheetContent(
                linkTypes = bottomSheetUiState.linkTypes,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    linkTypes: List<LinkType>?,
    onEvent: (LaunchEvents) -> Unit
) {
    Column {
        LaunchBottomSheetCard(
            linkTypes = linkTypes,
            actionLinkClicked = { onEvent(LaunchEvents.OpenLinkEvent(it)) }
        )
        LaunchBottomSheetExitButton(
            actionExitClicked = {
                onEvent(LaunchEvents.DismissBottomSheetEvent)
            }
        )
    }
}