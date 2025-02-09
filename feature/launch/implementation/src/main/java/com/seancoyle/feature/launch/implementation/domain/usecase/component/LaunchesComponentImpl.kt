package com.seancoyle.feature.launch.implementation.domain.usecase.component

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetCompanyCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetSpaceXDataUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.PaginateLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.SaveLaunchPreferencesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import dagger.Lazy

internal class LaunchesComponentImpl @Inject constructor(
    private val getSpaceXDataUseCase: GetSpaceXDataUseCase,
    private val getLaunchesApiCacheUseCase: GetLaunchesApiAndCacheUseCase,
    private val paginateLaunchesCacheUseCase: Lazy<PaginateLaunchesCacheUseCase>,
    private val mergedLaunchesCacheUseCase: MergedLaunchesCacheUseCase,
    private val getCompanyCacheUseCase: GetCompanyCacheUseCase,
    private val getCompanyApiCacheUseCase: GetCompanyApiAndCacheUseCase,
    private val saveLaunchPreferencesUseCase: Lazy<SaveLaunchPreferencesUseCase>,
    private val getLaunchPreferencesUseCase: GetLaunchPreferencesUseCase
) : LaunchesComponent {

    override fun getLaunchesApiAndCacheUseCase(): Flow<Result<Unit, DataError>> {
        return getLaunchesApiCacheUseCase.invoke()
    }

    override fun getSpaceXDataUseCase(): Flow<Result<Unit, DataError>> {
        return getSpaceXDataUseCase.invoke()
    }

    override fun paginateLaunchesCacheUseCase(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<Result<List<LaunchTypes>, DataError>> {
        return paginateLaunchesCacheUseCase.get().invoke(
            launchYear = year,
            order = order,
            launchStatus = launchFilter,
            page = page
        )
    }

    override fun createMergedLaunchesCacheUseCase(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<Result<List<LaunchTypes>, DataError>> {
        return mergedLaunchesCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override fun getCompanyCacheUseCase(): Flow<Result<Company?, DataError>> {
        return getCompanyCacheUseCase.invoke()
    }

    override fun getCompanyApiAndCacheUseCase(): Flow<Result<Unit, DataError>> {
        return getCompanyApiCacheUseCase.invoke()
    }

    override suspend fun getLaunchPreferencesUseCase(): LaunchPrefs {
        return getLaunchPreferencesUseCase.invoke()
    }

    override suspend fun saveLaunchPreferencesUseCase(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        saveLaunchPreferencesUseCase.get().invoke(
            order = order,
            launchStatus = launchStatus,
            launchYear = launchYear
        )
    }
}