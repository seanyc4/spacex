package com.seancoyle.spacex.business.data.cache.abstraction.launch

import com.seancoyle.launch_domain.model.launch.LaunchModel

interface LaunchCacheDataSource {

    suspend fun insert(launch: LaunchModel): Long

    suspend fun deleteById(id: Int): Int

    suspend fun deleteList(launchList: List<LaunchModel>): Int

    suspend fun deleteAll()

    suspend fun getById(id: Int): LaunchModel?

    suspend fun getAll(): List<LaunchModel>?

    suspend fun getTotalEntries(): Int

    suspend fun insertLaunchList(launchList: List<LaunchModel>): LongArray

    suspend fun filterLaunchList(
        year: String,
        order: String,
        launchFilter: Int?,
        page: Int
    ): List<LaunchModel>?
}






