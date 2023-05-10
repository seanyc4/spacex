package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.domain.DataState
import com.seancoyle.core.domain.Event
import com.seancoyle.launch.api.domain.model.LaunchState
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