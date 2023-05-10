package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.launch.api.domain.model.LaunchState
import kotlinx.coroutines.flow.Flow

interface GetCompanyInfoFromCacheUseCase {
    operator fun invoke(
        event: Event
    ): Flow<DataState<LaunchState>?>
}