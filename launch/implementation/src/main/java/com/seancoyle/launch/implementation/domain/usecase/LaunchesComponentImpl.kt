package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LaunchesComponentImpl @Inject constructor(
    private val getLaunchesApiCacheUseCase: GetLaunchesApiAndCacheUseCase,
    private val sortAndFilterLaunchesCacheUseCase: SortAndFilterLaunchesCacheUseCase,
    private val createMergedLaunchesCacheUseCase: CreateMergedLaunchesCacheUseCase,
    private val getCompanyCacheUseCase: GetCompanyCacheUseCase,
    private val getCompanyApiCacheUseCase: GetCompanyApiAndCacheUseCase
) : LaunchesComponent {

    override fun getLaunchesApiAndCacheUseCase(): Flow<DataResult<List<Launch>>> {
        return getLaunchesApiCacheUseCase.invoke()
    }

    override fun sortAndFilterLaunchesCacheUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>?>> {
        return sortAndFilterLaunchesCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override fun createMergedLaunchesCacheUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>>> {
        return createMergedLaunchesCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override fun getCompanyCacheUseCase(): Flow<DataResult<Company?>> {
        return getCompanyCacheUseCase.invoke()
    }

    override fun getCompanyApiAndCacheUseCase(): Flow<DataResult<Company>> {
        return getCompanyApiCacheUseCase.invoke()
    }
}