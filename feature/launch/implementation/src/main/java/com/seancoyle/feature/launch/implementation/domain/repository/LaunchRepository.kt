package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.*
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes

internal interface LaunchRepository {
    suspend fun insertLaunchesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, LocalError>
    suspend fun getLaunchesApi(offset: Int): LaunchResult<List<LaunchTypes.Launch>, DataError>
    suspend fun deleteLaunhesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Int, LocalError>
    suspend fun deleteAllCache(): LaunchResult<Unit, LocalError>
    suspend fun deleteByIdCache(id: String): LaunchResult<Int, LocalError>
    suspend fun getByIdCache(id: String): LaunchResult<LaunchTypes.Launch?, LocalError>
    suspend fun getAllCache(): LaunchResult<List<LaunchTypes.Launch>, LocalError>
    suspend fun getTotalEntriesCache(): LaunchResult<Int, LocalError>
    suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes>, LocalError>
}
