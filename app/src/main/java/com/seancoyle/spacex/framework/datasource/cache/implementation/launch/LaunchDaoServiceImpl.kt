package com.seancoyle.spacex.framework.datasource.cache.implementation.launch

import com.seancoyle.spacex.business.domain.model.launch.LaunchModel
import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.returnOrderedQuery
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchEntityMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchDaoServiceImpl
@Inject
constructor(
    private val dao: LaunchDao,
    private val entityMapper: LaunchEntityMapper
) : LaunchDaoService {

    override suspend fun insert(launch: LaunchModel): Long {
        return dao.insert(
            entityMapper.mapToEntity(
                entity = launch
            )
        )
    }

    override suspend fun insertList(launchList: List<LaunchModel>): LongArray {
        return dao.insertList(
            entityMapper.domainListToEntityList(
                launchList = launchList
            )
        )
    }

    override suspend fun deleteList(launchList: List<LaunchModel>): Int {
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

    override suspend fun getById(id: Int): LaunchModel? {
        return dao.getById(id = id)?.let {
            entityMapper.mapFromEntity(it)
        }
    }

    override suspend fun getAll(): List<LaunchModel>? {
        return dao.getAll()?.let {
            entityMapper.entityListToDomainList(it)
        }
    }

    override suspend fun getTotalEntries(): Int {
        return dao.getTotalEntries()
    }

    override suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int
    ): List<LaunchModel>? {
        return dao.returnOrderedQuery(
            year = year,
            launchFilter = launchFilter,
            page = page,
            order = order
        )?.let {
            entityMapper.entityListToDomainList(it)
        }

    }

}













