package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.domain.DataState
import com.seancoyle.launch.api.presentation.LaunchUiState
import kotlinx.coroutines.flow.Flow

interface GetLaunchesFromNetworkAndInsertToCacheUseCase {
    operator fun invoke(): Flow<DataState<LaunchUiState.LaunchState>?>
}