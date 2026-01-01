package com.seancoyle.feature.launch.presentation.launch.state

import com.seancoyle.feature.launch.presentation.launch.model.LaunchUI

sealed interface LaunchUiState {
    data object Loading : LaunchUiState
    data class Success(val launch: LaunchUI) : LaunchUiState
    data class Error(val message: String) : LaunchUiState
}
