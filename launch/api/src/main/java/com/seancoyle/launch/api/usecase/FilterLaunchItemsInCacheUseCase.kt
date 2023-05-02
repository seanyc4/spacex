package com.seancoyle.launch.api.usecase

import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.StateEvent
import com.seancoyle.launch.api.model.LaunchViewState
import kotlinx.coroutines.flow.Flow

interface FilterLaunchItemsInCacheUseCase {
    operator fun invoke(
        year: String,
        order: String,
        launchFilter: Int?,
        page: Int,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?>
}