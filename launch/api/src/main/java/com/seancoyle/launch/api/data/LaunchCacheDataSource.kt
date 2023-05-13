package com.seancoyle.launch.api.data

import com.seancoyle.launch.api.domain.model.ViewModel

interface LaunchCacheDataSource {

    suspend fun insert(launch: ViewModel): Long

    suspend fun deleteById(id: Int): Int

    suspend fun deleteList(launches: List<ViewModel>): Int

    suspend fun deleteAll()

    suspend fun getById(id: Int): ViewModel?

    suspend fun getAll(): List<ViewModel>?

    suspend fun getTotalEntries(): Int

    suspend fun insertList(launches: List<ViewModel>): LongArray

    suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int
    ): List<ViewModel>?
}






