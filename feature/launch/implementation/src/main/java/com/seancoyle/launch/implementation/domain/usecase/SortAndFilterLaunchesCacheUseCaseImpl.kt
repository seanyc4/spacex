package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class SortAndFilterLaunchesCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource
) : SortAndFilterLaunchesCacheUseCase {

    override operator fun invoke(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Flow<DataResult<List<LaunchTypes>?, DataError>> = flow {
        emit(
            cacheDataSource.filterLaunchList(
                launchYear = launchYear,
                order = order,
                launchStatus = launchStatus,
                page = page
            )
        )
    }
}