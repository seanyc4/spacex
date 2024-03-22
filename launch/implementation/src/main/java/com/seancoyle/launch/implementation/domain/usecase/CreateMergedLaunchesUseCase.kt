package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.cache.CacheResult
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

internal interface CreateMergedLaunchesUseCase {
   suspend operator fun invoke(
      year: String?,
      order: String,
      launchFilter: Int?,
      page: Int?
   ): Flow<CacheResult<List<ViewType>>>
}