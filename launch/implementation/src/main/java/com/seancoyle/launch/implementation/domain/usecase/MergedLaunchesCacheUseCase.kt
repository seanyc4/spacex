package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

internal interface MergedLaunchesCacheUseCase {
   operator fun invoke(
       year: String,
       order: Order,
       launchFilter: LaunchStatus,
       page: Int
   ): Flow<DataResult<List<LaunchTypes>, DataError>>
}