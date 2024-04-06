package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LaunchesComponentImpl @Inject constructor(
    private val getLaunchesApiCacheUseCase: GetLaunchesApiAndCacheUseCase,
    private val sortAndFilterLaunchesCacheUseCase: SortAndFilterLaunchesCacheUseCase,
    private val mergedLaunchesCacheUseCase: MergedLaunchesCacheUseCase,
    private val getCompanyCacheUseCase: GetCompanyCacheUseCase,
    private val getCompanyApiCacheUseCase: GetCompanyApiAndCacheUseCase
) : LaunchesComponent {

    override fun getLaunchesApiAndCacheUseCase(): Flow<DataResult<List<LaunchTypes.Launch>, DataError>> {
        return getLaunchesApiCacheUseCase.invoke()
    }

    override fun sortAndFilterLaunchesCacheUseCase(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<DataResult<List<LaunchTypes>?, DataError>> {
        return sortAndFilterLaunchesCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override fun createMergedLaunchesCacheUseCase(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<DataResult<List<LaunchTypes>, DataError>> {
        return mergedLaunchesCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override fun getCompanyCacheUseCase(): Flow<DataResult<Company?, DataError>> {
        return getCompanyCacheUseCase.invoke()
    }

    override fun getCompanyApiAndCacheUseCase(): Flow<DataResult<Company, DataError>> {
        return getCompanyApiCacheUseCase.invoke()
    }
}