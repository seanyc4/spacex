package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.LaunchDetailDao
import com.seancoyle.feature.launch.data.repository.LaunchDetailLocalDataSource
import com.seancoyle.feature.launch.domain.model.Launch
import timber.log.Timber
import javax.inject.Inject

internal class LaunchDetailLocalDataSourceImpl @Inject constructor(
    private val launchDetailDao: LaunchDetailDao,
    private val crashlytics: Crashlytics
) : LaunchDetailLocalDataSource {

    override suspend fun getTotalEntries(): LaunchResult<Int, Throwable> {
        return runSuspendCatching {
            launchDetailDao.getTotalEntries()
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
            launchDetailDao.getById(id)
        }.fold(
            onSuccess = { result ->
                result?.let {
                    LaunchResult.Success(result.toDomain())
                } ?: LaunchResult.Success(null)
            },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun upsertLaunchDetail(launch: Launch): LaunchResult<Unit, Throwable> {
        return runSuspendCatching {
            launchDetailDao.upsert(launch.toEntity())
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
            launchDetailDao.upsertAll(launches.toEntity())
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
            launchDetailDao.deleteAll()
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
        launchDetailDao.refreshLaunches(launches.toEntity())
    }
}
