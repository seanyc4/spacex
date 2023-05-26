package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.daos.LaunchDao
import com.seancoyle.database.daos.returnOrderedQuery
import com.seancoyle.launch.contract.data.LaunchCacheDataSource
import com.seancoyle.launch.contract.domain.model.ViewModel
import javax.inject.Inject

class LaunchCacheDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val entityMapper: LaunchEntityMapper
) : LaunchCacheDataSource {

    override suspend fun insert(launch: ViewModel): Long {
        return dao.insert(
            entityMapper.mapToEntity(
                entity = launch
            )
        )
    }

    override suspend fun insertList(launches: List<ViewModel>): LongArray {
        return dao.insertList(
            entityMapper.mapDomainListToEntityList(
                launches = launches
            )
        )
    }

    override suspend fun deleteList(launches: List<ViewModel>): Int {
        val ids = launches.mapIndexed { _, item -> item.id }
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

    override suspend fun getById(id: Int): ViewModel? {
        return dao.getById(id = id)?.let {
            entityMapper.mapFromEntity(it)
        }
    }

    override suspend fun getAll(): List<ViewModel>? {
        return dao.getAll()?.let {
            entityMapper.mapEntityListToDomainList(it)
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
    ): List<ViewModel>? {
        return dao.returnOrderedQuery(
            year = year,
            launchFilter = launchFilter,
            page = page,
            order = order
        )?.let {
            entityMapper.mapEntityListToDomainList(it)
        }
    }
}