package com.seancoyle.feature.launch.implementation.domain.usecase.component

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.PaginateLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.SaveLaunchPreferencesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import dagger.Lazy

internal class LaunchesComponentImpl @Inject constructor(
    private val getLaunchesApiCacheUseCase: GetLaunchesApiAndCacheUseCase,
    private val paginateLaunchesCacheUseCase: Lazy<PaginateLaunchesCacheUseCase>,
    private val saveLaunchPreferencesUseCase: Lazy<SaveLaunchPreferencesUseCase>,
    private val getLaunchPreferencesUseCase: GetLaunchPreferencesUseCase
) : LaunchesComponent {

    override fun getLaunchesApiAndCacheUseCase(currentPage: Int): Flow<LaunchResult<List<LaunchTypes.Launch>, DataError>> {
        return getLaunchesApiCacheUseCase.invoke(currentPage)
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
