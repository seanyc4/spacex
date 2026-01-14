package com.seancoyle.feature.launch.data.local

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.database.dao.PastLaunchDao
import com.seancoyle.database.dao.PastRemoteKeyDao
import com.seancoyle.database.entities.LaunchStatusEntity
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.PastRemoteKeyEntity
import com.seancoyle.feature.launch.data.repository.PastLaunchesLocalDataSource
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import timber.log.Timber
import javax.inject.Inject

internal class PastLaunchesLocalDataSourceImpl @Inject constructor(
    private val launchDao: PastLaunchDao,
    private val remoteKeyDao: PastRemoteKeyDao,
    private val crashlytics: Crashlytics
) : PastLaunchesLocalDataSource {

    override suspend fun getRemoteKeys(): List<PastRemoteKeyEntity?> {
        return try {
            remoteKeyDao.getAll()
        } catch (e: Exception) {
            Timber.e(e)
            crashlytics.logException(e)
            emptyList()
        }
    }

    override suspend fun getRemoteKey(id: String): PastRemoteKeyEntity? {
        return try {
            remoteKeyDao.getById(id)
        } catch (e: Exception) {
            Timber.e(e)
            crashlytics.logException(e)
            null
        }
    }

    override suspend fun refreshWithKeys(
        launches: List<LaunchSummary>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String?,
        cachedLaunchStatus: String?
    ) {
        val entities = launches.map { it.toPastEntity() }
        val remoteKeys = launches.map { launch ->
            PastRemoteKeyEntity(
                id = launch.id,
                nextKey = nextPage,
                prevKey = prevPage,
                currentPage = currentPage,
                cachedQuery = cachedQuery,
                cachedLaunchStatus = cachedLaunchStatus
            )
        }

        launchDao.refreshLaunchesWithKeys(
            launches = entities,
            remoteKeyDao = remoteKeyDao,
            remoteKeys = remoteKeys,
            nextPage = nextPage,
            prevPage = prevPage,
            currentPage = currentPage
        )
    }

    override suspend fun appendWithKeys(
        launches: List<LaunchSummary>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String?,
        cachedLaunchStatus: String?
    ) {
        val entities = launches.map { it.toPastEntity() }
        val remoteKeys = launches.map { launch ->
            PastRemoteKeyEntity(
                id = launch.id,
                nextKey = nextPage,
                prevKey = prevPage,
                currentPage = currentPage,
                cachedQuery = cachedQuery,
                cachedLaunchStatus = cachedLaunchStatus
            )
        }
        launchDao.upsertAll(entities)
        remoteKeyDao.upsertAll(remoteKeys)
    }

    override suspend fun getTotalEntries(): Int {
        return try {
            launchDao.getTotalEntries()
        } catch (e: Exception) {
            Timber.e(e)
            crashlytics.logException(e)
            0
        }
    }

    override suspend fun deleteAll() {
        launchDao.deleteAll()
        remoteKeyDao.deleteAll()
    }

    private fun LaunchSummary.toPastEntity(): PastLaunchEntity {
        return PastLaunchEntity(
            id = id,
            missionName = missionName,
            net = net,
            imageUrl = imageUrl,
            status = LaunchStatusEntity(
                id = status.id,
                name = status.name,
                abbrev = status.abbrev,
                description = status.description
            )
        )
    }

    override suspend fun refreshPastLaunches(launches: List<LaunchSummary>) {
        launchDao.refreshLaunches(launches.toPastEntity())
    }
}
