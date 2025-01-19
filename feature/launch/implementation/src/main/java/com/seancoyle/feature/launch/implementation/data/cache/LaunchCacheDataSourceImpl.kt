package com.seancoyle.feature.launch.implementation.data.cache

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.domain.Order
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.paginateLaunches
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.cache.LaunchCacheDataSource
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

    override suspend fun paginateLaunches(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Result<List<LaunchTypes>, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            val launchStatusEntity = mapper.mapToLaunchStatusEntity(launchStatus)
            mapper.entityToDomainList(
                dao.paginateLaunches(
                    launchYear = launchYear,
                    launchStatus = launchStatusEntity,
                    page = page,
                    order = order,
                    pageSize = PAGINATION_PAGE_SIZE
                )
            )
        }
    }

    override suspend fun insert(launch: LaunchTypes.Launch): Result<Long, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insert(mapper.mapToEntity(launch))
        }
    }

    override suspend fun insertList(launches: List<LaunchTypes.Launch>): Result<LongArray, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insertList(
                mapper.domainToEntityList(launches)
            )
        }
    }

    override suspend fun deleteList(launches: List<LaunchTypes.Launch>): Result<Int, DataError> {
        val ids = launches.mapIndexed { _, item -> item.id }
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteList(ids)
        }
    }

    override suspend fun deleteAll(): Result<Unit, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteAll()
        }
    }

    override suspend fun deleteById(id: String): Result<Int, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.deleteById(id)
        }
    }

    override suspend fun getById(id: String): Result<LaunchTypes.Launch?, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getById(id)?.let {
                mapper.mapFromEntity(it)
            }
        }
    }

    override suspend fun getAll(): Result<List<LaunchTypes>, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            mapper.entityToDomainList(dao.getAll())
        }
    }

    override suspend fun getTotalEntries(): Result<Int, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getTotalEntries()
        }
    }
}