package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

interface CreateMergedLaunchesUseCase {
   suspend operator fun invoke(
      year: String?,
      order: String,
      launchFilter: Int?,
      page: Int?
   ): Flow<List<ViewType>>
}