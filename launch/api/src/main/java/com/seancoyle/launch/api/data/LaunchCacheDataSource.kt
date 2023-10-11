package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

interface LaunchCacheDataSource {

    fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<ViewType>?>

    suspend fun insert(launch: Launch): Long

    suspend fun deleteById(id: Int): Int

    suspend fun deleteList(launches: List<Launch>): Int

    suspend fun deleteAll()

    suspend fun getById(id: Int): Launch?

    fun getAll(): Flow<List<ViewType>?>

    suspend fun getTotalEntries(): Int

    suspend fun insertList(launches: List<Launch>): LongArray

}