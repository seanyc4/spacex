package com.seancoyle.launch.api.usecase

import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.StateEvent
import com.seancoyle.launch.api.model.LaunchOptions
import com.seancoyle.launch.api.model.LaunchViewState
import kotlinx.coroutines.flow.Flow

interface GetLaunchListFromNetworkAndInsertToCacheUseCase {
    operator fun invoke(
        launchOptions: LaunchOptions,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?>
}