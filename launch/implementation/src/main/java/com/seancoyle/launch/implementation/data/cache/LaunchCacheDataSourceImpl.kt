package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.core_database.api.LaunchDao
import com.seancoyle.core_database.api.returnOrderedQuery
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import javax.inject.Inject

internal class LaunchCacheDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val entityMapper: LaunchEntityMapperImpl
) : LaunchCacheDataSource {

    override suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): List<ViewType>? {
        return dao.returnOrderedQuery(
            year = year,
            launchFilter = launchFilter,
            page = page,
            order = order
        )?.let {
            entityMapper.mapEntityListToDomainList(it)
        }
    }

    override suspend fun insert(launch: Launch): Long {
        return dao.insert(
            entityMapper.mapToEntity(entity = launch)
        )
    }

    override suspend fun insertList(launches: List<Launch>): LongArray {
        return dao.insertList(
            entityMapper.mapDomainListToEntityList(launches = launches)
        )
    }

    override suspend fun deleteList(launches: List<Launch>): Int {
        val ids = launches.mapIndexed { _, item -> item.id }
        return dao.deleteList(ids = ids)
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }

    override suspend fun deleteById(id: String): Int {
        return dao.deleteById(id = id)
    }

    override suspend fun getById(id: String): Launch? {
        return dao.getById(id = id)?.let {
            entityMapper.mapFromEntity(it)
        }
    }

    override suspend fun getAll(): List<ViewType>? {
        return dao.getAll()?.let {
            entityMapper.mapEntityListToDomainList(it)
        }
    }

    override suspend fun getTotalEntries(): Int {
        return dao.getTotalEntries()
    }
}