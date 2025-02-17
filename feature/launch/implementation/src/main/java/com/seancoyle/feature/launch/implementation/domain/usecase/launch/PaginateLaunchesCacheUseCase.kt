package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

internal interface PaginateLaunchesCacheUseCase {
    operator fun invoke(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Flow<LaunchResult<List<LaunchTypes>, LocalError>>
}