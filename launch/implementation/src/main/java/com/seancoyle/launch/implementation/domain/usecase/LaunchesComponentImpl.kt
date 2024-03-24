package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LaunchesComponentImpl @Inject constructor(
    private val getLaunchesApiAndCacheUseCase: GetLaunchesApiAndCacheUseCase,
    private val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase,
    private val createMergedAndFilteredLaunchesUseCase: CreateMergedLaunchesUseCase,
    private val getCompanyFromCacheUseCase: GetCompanyFromCacheUseCase,
    private val getCompanyApiAndCacheUseCase: GetCompanyApiAndCacheUseCase
) : LaunchesComponent {

    override suspend fun getLaunchesApiAndCacheUseCase(): Flow<DataResult<List<Launch>>> {
        return getLaunchesApiAndCacheUseCase.invoke()
    }

    override suspend fun filterLaunchItemsInCacheUseCase(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<DataResult<List<ViewType>?>> {
        return filterLaunchItemsInCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override suspend fun createMergeAndFilteredLaunchesUseCase(
        year: String?,
        order: String,
        launchFilter: Int?,
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