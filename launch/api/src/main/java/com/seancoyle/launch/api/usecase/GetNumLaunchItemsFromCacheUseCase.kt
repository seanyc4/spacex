package com.seancoyle.launch.api.usecase

import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.launch.api.model.LaunchState
import kotlinx.coroutines.flow.Flow

interface GetNumLaunchItemsFromCacheUseCase {
    operator fun invoke(
        event: Event
    ): Flow<DataState<LaunchState>?>
}