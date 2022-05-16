package com.seancoyle.spacex.framework.datasource.cache.implementation.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.returnOrderedQuery
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchCacheMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchDaoServiceImpl
@Inject
constructor(
    private val dao: LaunchDao,
    private val cacheMapper: LaunchCacheMapper
) : LaunchDaoService {

    override suspend fun insert(launch: LaunchDomainEntity): Long {
        return dao.insert(
            cacheMapper.mapToEntity(
                entity = launch
            )
        )
    }

    override suspend fun insertList(launchList: List<LaunchDomainEntity>): LongArray {
        return dao.insertList(
            cacheMapper.domainListToEntityList(
                launchList = launchList
            )
        )
    }

    override suspend fun deleteList(launchList: List<LaunchDomainEntity>): Int {
        val ids = launchList.mapIndexed { _, item -> item.id }
        return dao.deleteList(
            ids = ids
        )
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }

    override suspend fun deleteById(id: Int): Int {
        return dao.deleteById(
            id = id
        )
    }

    override suspend fun getById(id: Int): LaunchDomainEntity? {
        return dao.getById(id = id)?.let {
            cacheMapper.mapFromEntity(it)
        }
    }

    override suspend fun getAll(): List<LaunchDomainEntity>? {
        return dao.getAll()?.let {
            cacheMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getTotalEntries(): Int {
        return dao.getTotalEntries()
    }

    override suspend fun returnOrderedQuery(
        year: String?,
        order: String,
        isLaunchSuccess: Int?,
        page: Int
    ): List<LaunchDomainEntity>? {
        return dao.returnOrderedQuery(
            year = year,
            isLaunchSuccess = isLaunchSuccess,
            page = page,
            order = order
        )?.let {
            cacheMapper.entityListToDomainList(it)
        }

    }

}













