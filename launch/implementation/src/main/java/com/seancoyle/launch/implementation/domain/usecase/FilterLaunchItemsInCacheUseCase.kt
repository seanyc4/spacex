package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

internal interface FilterLaunchItemsInCacheUseCase {
    suspend operator fun invoke(
        year: String? = "",
        order: String,
        launchFilter: Int? = null,
        page: Int? = 1
    ): Flow<List<ViewType>?>
}