package com.seancoyle.feature.launch.implementation.data.network

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes

internal interface LaunchCacheDataSource {

    suspend fun paginateLaunches(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Result<List<LaunchTypes>, DataError>

    suspend fun insert(launch: LaunchTypes.Launch): Result<Long, DataError>

    suspend fun deleteById(id: String): Result<Int, DataError>

    suspend fun deleteList(launches: List<LaunchTypes.Launch>): Result<Int, DataError>

    suspend fun deleteAll(): Result<Unit, DataError>

    suspend fun getById(id: String): Result<LaunchTypes.Launch?, DataError>

    suspend fun getAll(): Result<List<LaunchTypes>, DataError>

    suspend fun getTotalEntries(): Result<Int, DataError>

    suspend fun insertList(launches: List<LaunchTypes.Launch>): Result<LongArray, DataError>

}