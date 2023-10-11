package com.seancoyle.launch.implementation.domain


import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.FilterLaunchItemsInCacheUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilterLaunchItemsInCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource,
) : FilterLaunchItemsInCacheUseCase {

    override suspend operator fun invoke(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<ViewType>?> = flow {
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