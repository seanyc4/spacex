package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType

internal interface LaunchCacheDataSource {

    suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): DataResult<List<ViewType>?>

    suspend fun insert(launch: Launch): DataResult<Long>

    suspend fun deleteById(id: String): DataResult<Int>

    suspend fun deleteList(launches: List<Launch>): DataResult<Int>

    suspend fun deleteAll()

    suspend fun getById(id: String): DataResult<Launch?>

    suspend fun getAll(): DataResult<List<ViewType>?>

    suspend fun getTotalEntries(): DataResult<Int>

    suspend fun insertList(launches: List<Launch>): DataResult<LongArray>

}