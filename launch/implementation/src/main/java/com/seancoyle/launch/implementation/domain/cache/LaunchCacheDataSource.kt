package com.seancoyle.launch.implementation.domain.cache

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.core.domain.Order
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes

internal interface LaunchCacheDataSource {

    suspend fun filterLaunchList(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): DataResult<List<LaunchTypes>?, DataError>

    suspend fun insert(launch: LaunchTypes.Launch): DataResult<Long, DataError>

    suspend fun deleteById(id: String): DataResult<Int, DataError>

    suspend fun deleteList(launches: List<LaunchTypes.Launch>): DataResult<Int, DataError>

    suspend fun deleteAll(): DataResult<Unit, DataError>

    suspend fun getById(id: String): DataResult<LaunchTypes.Launch?, DataError>

    suspend fun getAll(): DataResult<List<LaunchTypes>?, DataError>

    suspend fun getTotalEntries(): DataResult<Int, DataError>

    suspend fun insertList(launches: List<LaunchTypes.Launch>): DataResult<LongArray, DataError>

}