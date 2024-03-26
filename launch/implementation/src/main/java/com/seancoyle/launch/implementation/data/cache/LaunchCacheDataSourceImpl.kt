package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.Crashlytics
import com.seancoyle.core_database.api.LaunchDao
import com.seancoyle.core_database.api.returnOrderedQuery
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LaunchCacheDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val entityMapper: LaunchEntityMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchCacheDataSource {

    override suspend fun filterLaunchList(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): DataResult<List<ViewType>?> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.returnOrderedQuery(
                year = year,
                launchFilter = launchFilter,
                page = page,
                order = order
            )?.let {
                entityMapper.mapEntityListToDomainList(it)
            }
        }
    }

    override suspend fun insert(launch: Launch): DataResult<Long> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insert(entityMapper.mapToEntity(launch))
        }
    }

    override suspend fun insertList(launches: List<Launch>): DataResult<LongArray> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insertList(
                entityMapper.mapDomainListToEntityList(launches)
            )
        }
    }

    override suspend fun deleteList(launches: List<Launch>): DataResult<Int> {
        val ids = launches.mapIndexed { _, item -> item.id }
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteList(ids)
        }
    }

    override suspend fun deleteAll(): DataResult<Unit> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteAll()
        }
    }

    override suspend fun deleteById(id: String): DataResult<Int> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteById(id)
        }
    }

    override suspend fun getById(id: String): DataResult<Launch?> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getById(id)?.let {
                entityMapper.mapFromEntity(it)
            }
        }
    }

    override suspend fun getAll(): DataResult<List<ViewType>?> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getAll()?.let {
                entityMapper.mapEntityListToDomainList(it)
            }
        }
    }

    override suspend fun getTotalEntries(): DataResult<Int> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getTotalEntries() ?: 0
        }
    }
}