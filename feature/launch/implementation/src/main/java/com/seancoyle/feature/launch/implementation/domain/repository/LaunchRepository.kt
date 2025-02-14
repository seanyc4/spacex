package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchRepository {
    suspend fun insertLaunchesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, DataSourceError>
    suspend fun getLaunchesApi(launchOptions: LaunchOptions): LaunchResult<List<LaunchTypes.Launch>, DataSourceError>
    suspend fun deleteLaunhesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Int, DataSourceError>
    suspend fun deleteAllCache(): LaunchResult<Unit, DataSourceError>
    suspend fun deleteByIdCache(id: String): LaunchResult<Int, DataSourceError>
    suspend fun getByIdCache(id: String): LaunchResult<LaunchTypes.Launch?, DataSourceError>
    suspend fun getAllCache(): LaunchResult<List<LaunchTypes>, DataSourceError>
    suspend fun getTotalEntriesCache(): LaunchResult<Int, DataSourceError>
    suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes>, DataSourceError>
}