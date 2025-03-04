package com.seancoyle.feature.launch.implementation.data.cache.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.domain.Order
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.paginateLaunches
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchLocalDataSource
import timber.log.Timber
import javax.inject.Inject

internal class LaunchLocalDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val crashlytics: Crashlytics
) : LaunchLocalDataSource {

    override suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatusEntity,
        page: Int
    ): Result<List<LaunchEntity>> {
        return runCatching {
            dao.paginateLaunches(
                launchYear = launchYear,
                launchStatus = launchStatus,
                page = page,
                order = order,
                pageSize = PAGINATION_PAGE_SIZE
            )
        }.fold(
            onSuccess = { Result.success(it) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                Result.failure(it)
            }
        )
    }

    override suspend fun insert(launch: LaunchEntity): Result<Unit> {
        return runCatching { dao.insert(launch) }
            .fold(
                onSuccess = { Result.success(Unit) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    Result.failure(it)
                }
            )
    }

    override suspend fun insertList(launches: List<LaunchEntity>): Result<Unit> {
        return runCatching { dao.insertList(launches) }
            .fold(
                onSuccess = { Result.success(Unit) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    Result.failure(it)
                }
            )
    }

    override suspend fun deleteList(launches: List<LaunchEntity>): Result<Int> {
        val ids = launches.map { it.id }
        return runCatching { dao.deleteList(ids) }
            .fold(
                onSuccess = { Result.success(it) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    Result.failure(it)
                }
            )
    }

    override suspend fun deleteAll(): Result<Unit> {
        return runCatching { dao.deleteAll() }
            .fold(
                onSuccess = { Result.success(Unit) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    Result.failure(it)
                }
            )
    }

    override suspend fun deleteById(id: String): Result<Int> {
        return runCatching { dao.deleteById(id) }
            .fold(
                onSuccess = { Result.success(it) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    Result.failure(it)
                }
            )
    }

    override suspend fun getById(id: String): Result<LaunchEntity?> {
        return runCatching { dao.getById(id) }
            .fold(
                onSuccess = { Result.success(it) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    Result.failure(it)
                }
            )
    }

    override suspend fun getAll(): Result<List<LaunchEntity>> {
        return runCatching { dao.getAll() }
            .fold(
                onSuccess = { Result.success(it) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    Result.failure(it)
                }
            )
    }

    override suspend fun getTotalEntries(): Result<Int> {
        return runCatching { dao.getTotalEntries() }
            .fold(
                onSuccess = { Result.success(it) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    Result.failure(it)
                }
            )
    }

}