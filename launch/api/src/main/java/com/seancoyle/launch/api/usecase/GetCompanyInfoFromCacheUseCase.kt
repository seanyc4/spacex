package com.seancoyle.launch.api.usecase

import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.StateEvent
import com.seancoyle.launch.api.model.LaunchViewState
import kotlinx.coroutines.flow.Flow

interface GetCompanyInfoFromCacheUseCase {
    operator fun invoke(
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?>
}