package com.seancoyle.launch.implementation.data.cache

import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.core.domain.Crashlytics
import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.core.domain.Order
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.returnOrderedQuery
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LaunchCacheDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val mapper: LaunchEntityMapper,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchCacheDataSource {

    override suspend fun filterLaunchList(
        year: String,
        order: Order,
        launchFilter: LaunchStatus,
        page: Int
    ): DataResult<List<LaunchTypes>?, DataError> {
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
                mapper.mapEntityListToDomainList(it)
            }
        }
    }

    override suspend fun insert(launch: LaunchTypes.Launch): DataResult<Long, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insert(mapper.mapToEntity(launch))
        }
    }

    override suspend fun insertList(launches: List<LaunchTypes.Launch>): DataResult<LongArray, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insertList(
                mapper.mapDomainListToEntityList(launches)
            )
        }
    }

    override suspend fun deleteList(launches: List<LaunchTypes.Launch>): DataResult<Int, DataError> {
        val ids = launches.mapIndexed { _, item -> item.id }
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteList(ids)
        }
    }

    override suspend fun deleteAll(): DataResult<Unit, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteAll()
        }
    }

    override suspend fun deleteById(id: String): DataResult<Int, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteById(id)
        }
    }

    override suspend fun getById(id: String): DataResult<LaunchTypes.Launch?, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getById(id)?.let {
                mapper.mapFromEntity(it)
            }
        }
    }

    override suspend fun getAll(): DataResult<List<LaunchTypes>?, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getAll()?.let {
                mapper.mapEntityListToDomainList(it)
            }
        }
    }

    override suspend fun getTotalEntries(): DataResult<Int, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getTotalEntries()
        }
    }
}