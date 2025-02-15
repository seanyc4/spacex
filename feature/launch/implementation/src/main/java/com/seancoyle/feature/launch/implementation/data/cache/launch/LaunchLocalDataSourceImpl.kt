package com.seancoyle.feature.launch.implementation.data.cache.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.di.IODispatcher
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.paginateLaunches
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.implementation.data.cache.company.LocalDataSourceErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

internal class LaunchLocalDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val crashlytics: Crashlytics,
    private val localDataSourceErrorMapper: LocalDataSourceErrorMapper,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : LaunchLocalDataSource {

    override suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatusEntity,
        page: Int
    ): LaunchResult<List<LaunchEntity>, LocalError> {
        return withContext(ioDispatcher) {
            runCatching {
                dao.paginateLaunches(
                    launchYear = launchYear,
                    launchStatus = launchStatus,
                    page = page,
                    order = order,
                    pageSize = PAGINATION_PAGE_SIZE
                )
            }.fold(
                onSuccess = { LaunchResult.Success(it) },
                onFailure = { handleException(it) }
            )
        }
    }

    override suspend fun insert(launch: LaunchEntity): LaunchResult<Unit, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.insert(launch) }
                .fold(
                    onSuccess = { LaunchResult.Success(Unit) },
                    onFailure = { handleException(it) }
                )
        }
    }

    override suspend fun insertList(launches: List<LaunchEntity>): LaunchResult<Unit, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.insertList(launches) }
                .fold(
                    onSuccess = { LaunchResult.Success(Unit) },
                    onFailure = { handleException(it) }
                )
        }
    }

    override suspend fun deleteList(launches: List<LaunchEntity>): LaunchResult<Int, LocalError> {
        val ids = launches.map { it.id }
        return withContext(ioDispatcher) {
            runCatching { dao.deleteList(ids) }
                .fold(
                    onSuccess = { LaunchResult.Success(it) },
                    onFailure = { handleException(it) }
                )
        }
    }

    override suspend fun deleteAll(): LaunchResult<Unit, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.deleteAll() }
                .fold(
                    onSuccess = { LaunchResult.Success(Unit) },
                    onFailure = { handleException(it) }
                )
        }
    }

    override suspend fun deleteById(id: String): LaunchResult<Int, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.deleteById(id) }
                .fold(
                    onSuccess = { LaunchResult.Success(it) },
                    onFailure = { handleException(it) }
                )
        }
    }

    override suspend fun getById(id: String): LaunchResult<LaunchEntity?, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.getById(id) }
                .fold(
                    onSuccess = { LaunchResult.Success(it) },
                    onFailure = { handleException(it) }
                )
        }
    }

    override suspend fun getAll(): LaunchResult<List<LaunchEntity>, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.getAll() }
                .fold(
                    onSuccess = { LaunchResult.Success(it) },
                    onFailure = { handleException(it) }
                )
        }
    }

    override suspend fun getTotalEntries(): LaunchResult<Int, LocalError> {
        return withContext(ioDispatcher) {
            runCatching { dao.getTotalEntries() }
                .fold(
                    onSuccess = { LaunchResult.Success(it) },
                    onFailure = { handleException(it) }
                )
        }
    }

    private fun handleException(exception: Throwable): LaunchResult.Error<LocalError> {
        Timber.e(exception)
        crashlytics.logException(exception)
        return LaunchResult.Error(localDataSourceErrorMapper.map(exception))
    }
}
