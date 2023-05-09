package com.seancoyle.launch.api

import com.seancoyle.launch.api.model.LaunchModel

interface LaunchCacheDataSource {

    suspend fun insert(launch: LaunchModel): Long

    suspend fun deleteById(id: Int): Int

    suspend fun deleteList(launches: List<LaunchModel>): Int

    suspend fun deleteAll()

    suspend fun getById(id: Int): LaunchModel?

    suspend fun getAll(): List<LaunchModel>?

    suspend fun getTotalEntries(): Int

    suspend fun insertList(launches: List<LaunchModel>): LongArray

    suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int
    ): List<LaunchModel>?
}






