package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.Launch

interface LaunchCacheDataSource {

    suspend fun insert(launch: Launch): Long

    suspend fun deleteById(id: Int): Int

    suspend fun deleteList(launches: List<Launch>): Int

    suspend fun deleteAll()

    suspend fun getById(id: Int): Launch?

    suspend fun getAll(): List<Launch>?

    suspend fun getTotalEntries(): Int

    suspend fun insertList(launches: List<Launch>): LongArray

    suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int
    ): List<Launch>?

}