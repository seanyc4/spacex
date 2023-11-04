package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType

interface LaunchCacheDataSource {

    suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): List<ViewType>?

    suspend fun insert(launch: Launch): Long

    suspend fun deleteById(id: String): Int

    suspend fun deleteList(launches: List<Launch>): Int

    suspend fun deleteAll()

    suspend fun getById(id: String): Launch?

    suspend fun getAll(): List<ViewType>?

    suspend fun getTotalEntries(): Int

    suspend fun insertList(launches: List<Launch>): LongArray

}