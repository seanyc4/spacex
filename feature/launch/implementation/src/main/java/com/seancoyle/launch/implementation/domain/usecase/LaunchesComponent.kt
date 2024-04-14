package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.model.LaunchPrefs
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

interface LaunchesComponent {

    fun sortAndFilterLaunchesCacheUseCase(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<Result<List<LaunchTypes>?, DataError>>

    fun createMergedLaunchesCacheUseCase(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<Result<List<LaunchTypes>, DataError>>

    fun getLaunchesApiAndCacheUseCase(): Flow<Result<List<LaunchTypes.Launch>, DataError>>

    fun getCompanyCacheUseCase(): Flow<Result<Company?, DataError>>

    fun getCompanyApiAndCacheUseCase(): Flow<Result<Company, DataError>>

    suspend fun getLaunchPreferencesUseCase(): LaunchPrefs

    suspend fun saveLaunchPreferencesUseCase(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    )
}