package com.seancoyle.launch.implementation.domain.cache

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.ViewType

internal interface LaunchCacheDataSource {

    suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: LaunchStatus,
        page: Int?
    ): DataResult<List<ViewType>?, DataError>

    suspend fun insert(launch: Launch): DataResult<Long, DataError>

    suspend fun deleteById(id: String): DataResult<Int, DataError>

    suspend fun deleteList(launches: List<Launch>): DataResult<Int, DataError>

    suspend fun deleteAll(): DataResult<Unit, DataError>

    suspend fun getById(id: String): DataResult<Launch?, DataError>

    suspend fun getAll(): DataResult<List<ViewType>?, DataError>

    suspend fun getTotalEntries(): DataResult<Int, DataError>

    suspend fun insertList(launches: List<Launch>): DataResult<LongArray, DataError>

}