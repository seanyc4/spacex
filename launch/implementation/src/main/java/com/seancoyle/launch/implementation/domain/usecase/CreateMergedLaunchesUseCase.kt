package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

internal interface CreateMergedLaunchesUseCase {
   suspend operator fun invoke(
      year: String?,
      order: String,
      launchFilter: Int?,
      page: Int?
   ): Flow<DataResult<List<ViewType>>>
}