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
    private val getLaunchesApiAndCacheUseCase: GetLaunchesApiAndCacheUseCase,
    private val filterLaunchesCacheUseCase: FilterLaunchesCacheUseCase,
    private val createMergedAndFilteredLaunchesUseCase: CreateMergedLaunchesUseCase,
    private val getCompanyFromCacheUseCase: GetCompanyFromCacheUseCase,
    private val getCompanyApiAndCacheUseCase: GetCompanyApiAndCacheUseCase
) : LaunchesComponent {

    override fun getLaunchesApiAndCacheUseCase(): Flow<DataResult<List<Launch>>> {
        return getLaunchesApiAndCacheUseCase.invoke()
    }

    override fun filterLaunchesCacheUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>?>> {
        return filterLaunchesCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override fun createMergeAndFilteredLaunchesUseCase(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): Flow<DataResult<List<ViewType>>> {
        return createMergedAndFilteredLaunchesUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override fun getCompanyInfoFromCacheUseCase(): Flow<DataResult<Company?>> {
        return getCompanyFromCacheUseCase.invoke()
    }

    override fun getCompanyInfoApiAndCacheUseCase(): Flow<DataResult<Company>> {
        return getCompanyApiAndCacheUseCase.invoke()
    }
}