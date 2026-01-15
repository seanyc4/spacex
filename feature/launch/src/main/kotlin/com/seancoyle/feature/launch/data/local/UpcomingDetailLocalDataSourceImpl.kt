package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.UpcomingDetailDao
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.domain.model.Launch
import timber.log.Timber
import javax.inject.Inject

internal class UpcomingDetailLocalDataSourceImpl @Inject constructor(
    private val upcomingDetailDao: UpcomingDetailDao,
    private val crashlytics: Crashlytics,
) : DetailLocalDataSource {

    override suspend fun getTotalEntries(): LaunchResult<Int, Throwable> {
        return runSuspendCatching {
            upcomingDetailDao.getTotalEntries()
        }.fold(
            onSuccess = { LaunchResult.Success(it) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun getLaunchDetail(id: String): LaunchResult<Launch?, Throwable> {
        return runSuspendCatching {
            upcomingDetailDao.getById(id)?.toDomain()
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
            upcomingDetailDao.upsert(launch.toUpcomingDetailEntity())
        }.fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun upsertAllLaunchDetails(launches: List<Launch>): LaunchResult<Unit, Throwable> {
        return runSuspendCatching {
            upcomingDetailDao.upsertAll(launches.toUpcomingDetailEntity())
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
            upcomingDetailDao.deleteAll()
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
        upcomingDetailDao.refreshLaunches(launches.toUpcomingDetailEntity())
    }
}
