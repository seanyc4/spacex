package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchRepository {
    suspend fun insertLaunchesCache(launches: List<LaunchTypes.Launch>): Result<Unit, DataError>
    suspend fun getLaunchesApi(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError>
    suspend fun deleteLaunhesCache(launches: List<LaunchTypes.Launch>): Result<Int, DataError>
    suspend fun deleteAllCache(): Result<Unit, DataError>
    suspend fun deleteByIdCache(id: String): Result<Int, DataError>
    suspend fun getByIdCache(id: String): Result<LaunchTypes.Launch?, DataError>
    suspend fun getAllCache(): Result<List<LaunchTypes>, DataError>
    suspend fun getTotalEntriesCache(): Result<Int, DataError>
    suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Result<List<LaunchTypes>, DataError>
}