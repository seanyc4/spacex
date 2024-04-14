package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
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
    ): Flow<DataResult<List<LaunchTypes>?, DataError>>

    fun createMergedLaunchesCacheUseCase(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): Flow<DataResult<List<LaunchTypes>, DataError>>

    fun getLaunchesApiAndCacheUseCase(): Flow<DataResult<List<LaunchTypes.Launch>, DataError>>

    fun getCompanyCacheUseCase(): Flow<DataResult<Company?, DataError>>

    fun getCompanyApiAndCacheUseCase(): Flow<DataResult<Company, DataError>>

    suspend fun getLaunchPreferencesUseCase(): LaunchPrefs

    suspend fun saveLaunchPreferencesUseCase(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    )
}