package com.seancoyle.feature.launch.implementation.domain.usecase.component

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetSpaceXDataUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.PaginateLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.SaveLaunchPreferencesUseCase
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

    override fun getLaunchesApiAndCacheUseCase(): Flow<LaunchResult<Unit, DataError>> {
        return getLaunchesApiCacheUseCase.invoke()
    }

    override fun getSpaceXDataUseCase(): Flow<LaunchResult<Unit, DataError>> {
        return getSpaceXDataUseCase.invoke()
    }

    override fun paginateLaunchesCacheUseCase(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<LaunchResult<List<LaunchTypes>, LocalError>> {
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
    ): Flow<LaunchResult<List<LaunchTypes>, LocalError>> {
        return mergedLaunchesCacheUseCase.invoke(
            year = year,
            order = order,
            launchFilter = launchFilter,
            page = page
        )
    }

    override fun getCompanyCacheUseCase(): Flow<LaunchResult<Company?, LocalError>> {
        return getCompanyCacheUseCase.invoke()
    }

    override fun getCompanyApiAndCacheUseCase(): Flow<LaunchResult<Unit, DataError>> {
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