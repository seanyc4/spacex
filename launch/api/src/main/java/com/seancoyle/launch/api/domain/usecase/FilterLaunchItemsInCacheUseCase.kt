package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.domain.DataState
import com.seancoyle.launch.api.presentation.LaunchUiState
import kotlinx.coroutines.flow.Flow

interface FilterLaunchItemsInCacheUseCase {
    operator fun invoke(
        year: String,
        order: String,
        launchFilter: Int?,
        page: Int
    ): Flow<DataState<LaunchUiState.LaunchState>?>
}