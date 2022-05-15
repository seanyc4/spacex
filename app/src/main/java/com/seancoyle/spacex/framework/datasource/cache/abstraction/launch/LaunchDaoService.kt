package com.seancoyle.spacex.framework.datasource.cache.abstraction.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LAUNCH_PAGINATION_PAGE_SIZE

interface LaunchDaoService {

    suspend fun insert(launch: LaunchDomainEntity): Long

    suspend fun insertList(launchList: List<LaunchDomainEntity>): LongArray

    suspend fun deleteById(id: Int): Int

    suspend fun deleteList(launchList: List<LaunchDomainEntity>) : Int

    suspend fun deleteAll()

    suspend fun getById(id: Int): LaunchDomainEntity?

    suspend fun getAll(): List<LaunchDomainEntity>?

    suspend fun getTotalEntries(): Int

    suspend fun returnOrderedQuery(
        year: String?,
        order: String,
        isLaunchSuccess: Boolean?,
        page: Int
    ): List<LaunchDomainEntity>?

}












