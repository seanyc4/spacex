package com.seancoyle.launch_datasource.cache.implementation.launch

import com.seancoyle.database.daos.LaunchDao
import com.seancoyle.database.daos.returnOrderedQuery
import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource.cache.mappers.launch.LaunchEntityMapper
import com.seancoyle.launch_models.model.launch.LaunchModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaunchCacheDataSourceImpl
@Inject
constructor(
    private val dao: LaunchDao,
    private val entityMapper: LaunchEntityMapper
) : LaunchCacheDataSource {

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
        year: String,
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





















