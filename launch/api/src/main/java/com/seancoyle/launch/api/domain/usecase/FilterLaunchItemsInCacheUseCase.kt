package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

interface FilterLaunchItemsInCacheUseCase {
    operator fun invoke(
        year: String? = "",
        order: String,
        launchFilter: Int? = null,
        page: Int? = 1
    ): Flow<List<ViewType>?>
}