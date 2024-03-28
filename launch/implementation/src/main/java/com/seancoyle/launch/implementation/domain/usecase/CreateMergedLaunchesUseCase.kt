package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

internal interface CreateMergedLaunchesUseCase {
   operator fun invoke(
       year: String?,
       order: String,
       launchFilter: LaunchStatus,
       page: Int?
   ): Flow<DataResult<List<ViewType>>>
}