package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.Launch
import kotlinx.coroutines.flow.Flow

interface FilterLaunchItemsInCacheUseCase {
    suspend operator fun invoke(
        year: String,
        order: String,
        launchFilter: Int?,
        page: Int
    ): Flow<List<Launch>>
}