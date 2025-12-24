package com.seancoyle.feature.launch.implementation.data.local

import com.seancoyle.core.common.coroutines.runSuspendCatching
import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.LaunchRemoteKeyDao
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.feature.launch.implementation.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.repository.LaunchLocalDataSource
import timber.log.Timber
import javax.inject.Inject

internal class LaunchLocalDataSourceImpl @Inject constructor(
    private val launchDao: LaunchDao,
    private val remoteKeyDao: LaunchRemoteKeyDao,
    private val crashlytics: Crashlytics
) : LaunchLocalDataSource {

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
        launches: List<LaunchTypes.Launch>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String?,
        cachedOrder: String?
    ) {
        // Create a remote key for each launch item
        val remoteKeys = launches.map { launch ->
            LaunchRemoteKeyEntity(
                id = launch.id,
                nextKey = nextPage,
                prevKey = prevPage,
                currentPage = currentPage,
                cachedQuery = cachedQuery,
                cachedOrder = cachedOrder
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
        launches: List<LaunchTypes.Launch>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String?,
        cachedOrder: String?
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
                cachedOrder = cachedOrder
            )
        }
        remoteKeyDao.upsertAll(remoteKeys)
    }

    override suspend fun refreshLaunches(launches: List<LaunchTypes.Launch>) {
        launchDao.refreshLaunches(launches.toEntity())
    }

    override suspend fun upsert(launch: LaunchTypes.Launch): LaunchResult<Unit, Throwable> {
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

    override suspend fun upsertAll(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, Throwable> {
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

    override suspend fun getById(id: String): LaunchResult<LaunchTypes.Launch?, Throwable> {
        return runSuspendCatching {
            launchDao.getById(id)
        }.fold(
            onSuccess = { result ->
                //  result?.let {
                LaunchResult.Success(result!!.toDomain())
                //   } ?: LaunchResult.Error(LocalError.CACHE_ERROR_END_REACHED)
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
