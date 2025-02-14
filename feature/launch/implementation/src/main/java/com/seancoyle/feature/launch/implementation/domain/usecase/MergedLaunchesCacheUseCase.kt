package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

internal interface MergedLaunchesCacheUseCase {
   operator fun invoke(
       year: String,
       order: Order,
       launchFilter: LaunchStatus,
       page: Int
   ): Flow<LaunchResult<List<LaunchTypes>, DataSourceError>>
}