package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.database.daos.LaunchDao
import com.seancoyle.database.daos.returnOrderedQuery
import com.seancoyle.launch.api.data.LaunchCacheDataSource
import com.seancoyle.launch.api.domain.model.Launch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LaunchCacheDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val entityMapper: LaunchEntityMapper
) : LaunchCacheDataSource {

    override fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<Launch>?> {
        return dao.returnOrderedQuery(
            year = year,
            launchFilter = launchFilter,
            page = page,
            order = order
        ).map {
            it?.let {
                entityMapper.mapEntityListToDomainList(it)
            }
        }
    }

    override suspend fun insert(launch: Launch): Long {
        return dao.insert(
            entityMapper.mapToEntity(
                entity = launch
            )
        )
    }

    override suspend fun insertList(launches: List<Launch>): LongArray {
        return dao.insertList(
            entityMapper.mapDomainListToEntityList(
                launches = launches
            )
        )
    }

    override suspend fun deleteList(launches: List<Launch>): Int {
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

    override suspend fun getById(id: Int): Launch? {
        return dao.getById(id = id)?.let {
            entityMapper.mapFromEntity(it)
        }
    }

    override fun getAll(): Flow<List<Launch>?> {
        return dao.getAll().map {
            it?.let {
                entityMapper.mapEntityListToDomainList(it)
            }
        }
    }

    override suspend fun getTotalEntries(): Int {
        return dao.getTotalEntries()
    }
}