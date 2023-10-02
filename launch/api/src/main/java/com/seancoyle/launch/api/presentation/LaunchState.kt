package com.seancoyle.launch.api.presentation

import com.seancoyle.launch.api.domain.model.ViewType

sealed interface LaunchUiState {

    data object Loading : LaunchUiState

    data object Empty : LaunchUiState

    data class ErrorState(val message: String) : LaunchUiState

    data class LaunchState(
        val mergedLaunches: List<ViewType>,
    ) : LaunchUiState

}