package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LaunchesComponentImpl @Inject constructor(
    private val getLaunchesFromNetworkAndInsertToCacheUseCase: GetLaunchesFromNetworkAndInsertToCacheUseCase,
    private val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase,
    private val mergedLaunchesUseCase: CreateMergedLaunchesUseCase
) : LaunchesComponent {

    override suspend fun getLaunchesFromNetworkAndInsertToCacheUseCase(): Flow<ApiResult<List<Launch>>> {
        return getLaunchesFromNetworkAndInsertToCacheUseCase.invoke()
    }

    override suspend fun filterLaunchItemsInCacheUseCase(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<ViewType>?> {
        return filterLaunchItemsInCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override suspend fun createMergeLaunchesUseCase(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<ViewType>> {
        return mergedLaunchesUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }
}