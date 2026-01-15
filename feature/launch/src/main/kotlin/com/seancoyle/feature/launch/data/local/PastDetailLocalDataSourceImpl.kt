package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.PastDetailDao
import com.seancoyle.feature.launch.data.repository.PastDetailLocalDataSource
import com.seancoyle.feature.launch.domain.model.Launch
import timber.log.Timber
import javax.inject.Inject

internal class PastDetailLocalDataSourceImpl @Inject constructor(
    private val pastDetailDao: PastDetailDao,
    private val crashlytics: Crashlytics,
) : PastDetailLocalDataSource {

    override suspend fun upsertAllLaunchDetails(launches: List<Launch>): LaunchResult<Unit, Throwable> {
        return runSuspendCatching {
            pastDetailDao.upsertAll(launches.toPastDetailEntity())
        }.fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun deleteAllLaunchDetails(): LaunchResult<Unit, Throwable> {
        return runSuspendCatching {
            pastDetailDao.deleteAll()
        }.fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun refreshLaunches(launches: List<Launch>) {
        pastDetailDao.refreshLaunches(launches.toPastDetailEntity())
    }

    override suspend fun getTotalEntries(): LaunchResult<Int, Throwable> {
        return runSuspendCatching {
            pastDetailDao.getTotalEntries()
        }.fold(
            onSuccess = { count -> LaunchResult.Success(count) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun getLaunchDetail(id: String): LaunchResult<Launch?, Throwable> {
        return runSuspendCatching {
            pastDetailDao.getById(id)?.toDomain()
        }.fold(
            onSuccess = { LaunchResult.Success(it) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun upsertLaunchDetail(launch: Launch): LaunchResult<Unit, Throwable> {
        return runSuspendCatching {
            pastDetailDao.upsert(launch.toPastDetailEntity())
        }.fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }
}
