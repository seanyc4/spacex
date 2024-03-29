package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FilterLaunchesCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource
) : FilterLaunchesCacheUseCase {

    override operator fun invoke(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<DataResult<List<ViewType>?>> = flow {
        emit(
            cacheDataSource.filterLaunchList(
                year = year,
                order = order,
                launchFilter = launchFilter,
                page = page
            )
        )
    }
}