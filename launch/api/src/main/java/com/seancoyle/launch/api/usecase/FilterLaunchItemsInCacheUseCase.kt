package com.seancoyle.launch.api.usecase

import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.launch.api.model.LaunchState
import kotlinx.coroutines.flow.Flow

interface FilterLaunchItemsInCacheUseCase {
    operator fun invoke(
        year: String,
        order: String,
        launchFilter: Int?,
        page: Int,
        event: Event
    ): Flow<DataState<LaunchState>?>
}