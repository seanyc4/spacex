package com.seancoyle.launch.implementation.presentation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.seancoyle.core.presentation.StringResource
import com.seancoyle.core_ui.composables.CircularProgressBar
import com.seancoyle.core_ui.composables.DisplayNotification
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.implementation.presentation.state.LaunchesUiState
import kotlinx.coroutines.FlowPreview

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
            LaunchesContent(
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