package com.seancoyle.launch.contract.domain.usecase

import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.launch.contract.domain.model.LaunchState
import kotlinx.coroutines.flow.Flow

interface GetCompanyInfoFromCacheUseCase {
    operator fun invoke(
        event: Event
    ): Flow<DataState<LaunchState>?>
}