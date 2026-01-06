package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.LaunchRemoteKeyDao
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import timber.log.Timber
import javax.inject.Inject

internal class LaunchesLocalDataSourceImpl @Inject constructor(
    private val launchDao: LaunchDao,
    private val remoteKeyDao: LaunchRemoteKeyDao,
    private val crashlytics: Crashlytics
) : LaunchesLocalDataSource {

    override suspend fun getRemoteKeys(): List<LaunchRemoteKeyEntity?> {
        return runSuspendCatching {
            remoteKeyDao.getRemoteKeys()
        }.getOrElse {
            Timber.e(it)
            crashlytics.logException(it)
            emptyList()
        }
    }

    override suspend fun getRemoteKey(id: String): LaunchRemoteKeyEntity? {
        return runSuspendCatching {
            remoteKeyDao.getRemoteKey(id)
        }.getOrElse {
            Timber.e(it)
            crashlytics.logException(it)
            null
        }
    }

    override suspend fun refreshLaunchesWithKeys(
        launches: List<LaunchSummary>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String?,
        cachedLaunchType: String?
    ) {
        // Create a remote key for each launch item
        val remoteKeys = launches.map { launch ->
            LaunchRemoteKeyEntity(
                id = launch.id,
                nextKey = nextPage,
                prevKey = prevPage,
                currentPage = currentPage,
                cachedQuery = cachedQuery,
                cachedLaunchType = cachedLaunchType
            )
        }
        launchDao.refreshLaunchesWithKeys(
            launches = launches.toEntity(),
            remoteKeyDao = remoteKeyDao,
            nextPage = nextPage,
            prevPage = prevPage,
            currentPage = currentPage,
            remoteKeys = remoteKeys
        )
    }

    override suspend fun appendLaunchesWithKeys(
        launches: List<LaunchSummary>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String?,
        cachedLaunchType: String?
    ) {
        val launchEntities = launches.toEntity()
        launchDao.upsertAll(launchEntities)

        // Create a remote key for each launch item
        val remoteKeys = launchEntities.map { launch ->
            LaunchRemoteKeyEntity(
                id = launch.id,
                nextKey = nextPage,
                prevKey = prevPage,
                currentPage = currentPage,
                cachedQuery = cachedQuery,
                cachedLaunchType = cachedLaunchType
            )
        }
        remoteKeyDao.upsertAll(remoteKeys)
    }

    override suspend fun refreshLaunches(launches: List<LaunchSummary>) {
        launchDao.refreshLaunches(launches.toEntity())
    }

    override suspend fun upsert(launch: LaunchSummary): LaunchResult<Unit, Throwable> {
        return runSuspendCatching {
            launchDao.upsert(launch.toEntity())
        }.fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun upsertAll(launches: List<LaunchSummary>): LaunchResult<Unit, Throwable> {
        return runSuspendCatching {
            launchDao.upsertAll(launches.toEntity())
        }.fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun deleteAll(): LaunchResult<Unit, Throwable> {
        return runSuspendCatching {
            launchDao.deleteAll()
        }.fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun getById(id: String): LaunchResult<LaunchSummary?, Throwable> {
        return runSuspendCatching {
            launchDao.getById(id)
        }.fold(
            onSuccess = { result ->
                result?.let {
                    LaunchResult.Success(result.toDomain())
                } ?: LaunchResult.Error(Throwable("Item not found"))
            },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

    override suspend fun getTotalEntries(): LaunchResult<Int, Throwable> {
        return runSuspendCatching {
            launchDao.getTotalEntries()
        }.fold(
            onSuccess = { LaunchResult.Success(it) },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(it)
            }
        )
    }

}
