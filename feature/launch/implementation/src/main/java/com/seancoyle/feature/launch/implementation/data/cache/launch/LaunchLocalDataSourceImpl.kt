package com.seancoyle.feature.launch.implementation.data.cache.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.domain.Order
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.paginateLaunches
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchDiskDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class LaunchLocalDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val crashlytics: Crashlytics,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchDiskDataSource {

    override suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatusEntity,
        page: Int
    ): Result<List<LaunchEntity>, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.paginateLaunches(
                launchYear = launchYear,
                launchStatus = launchStatus,
                page = page,
                order = order,
                pageSize = PAGINATION_PAGE_SIZE
            )
        }
    }

    override suspend fun insert(launch: LaunchEntity): Result<Unit, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insert(launch)
        }
    }

    override suspend fun insertList(launches: List<LaunchEntity>): Result<Unit, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.insertList(launches)
        }
    }

    override suspend fun deleteList(launches: List<LaunchEntity>): Result<Int, DataError> {
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

    override suspend fun getById(id: String): Result<LaunchEntity?, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getById(id)
        }
    }

    override suspend fun getAll(): Result<List<LaunchEntity>, DataError> {
        return safeCacheCall(
            dispatcher = ioDispatcher,
            crashlytics = crashlytics
        ) {
            dao.getAll()
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