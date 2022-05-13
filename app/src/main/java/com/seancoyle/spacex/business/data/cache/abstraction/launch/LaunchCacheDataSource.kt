package com.seancoyle.spacex.business.data.cache.abstraction.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity

interface LaunchCacheDataSource {

    suspend fun insert(launch: LaunchDomainEntity): Long

    suspend fun deleteById(id: Int): Int

    suspend fun deleteList(launchList: List<LaunchDomainEntity>): Int

    suspend fun deleteAll()

    suspend fun getById(id: Int): LaunchDomainEntity?

    suspend fun getAll(): List<LaunchDomainEntity>?

    suspend fun getTotalEntries(): Int

    suspend fun insertLaunchList(launchList: List<LaunchDomainEntity>): LongArray

    suspend fun searchLaunchList(
        query: String,
        order: String,
        isLaunchSuccess: Boolean?,
        page: Int
    ): List<LaunchDomainEntity>?
}






