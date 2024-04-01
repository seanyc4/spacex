package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

internal interface SortAndFilterLaunchesCacheUseCase {
    operator fun invoke(
        year: String? = "",
        order: String,
        launchFilter: LaunchStatus,
        page: Int? = 1
    ): Flow<DataResult<List<LaunchTypes>?, DataError>>
}