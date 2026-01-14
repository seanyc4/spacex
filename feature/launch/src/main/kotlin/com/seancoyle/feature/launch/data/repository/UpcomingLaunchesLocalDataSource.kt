package com.seancoyle.feature.launch.data.repository

import com.seancoyle.database.entities.UpcomingRemoteKeyEntity
import com.seancoyle.feature.launch.domain.model.LaunchSummary

internal interface UpcomingLaunchesLocalDataSource {

    suspend fun getRemoteKeys(): List<UpcomingRemoteKeyEntity?>

    suspend fun getRemoteKey(id: String): UpcomingRemoteKeyEntity?

    suspend fun refreshWithKeys(
        launches: List<LaunchSummary>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String?,
        cachedLaunchStatus: String?
    )

    suspend fun appendWithKeys(
        launches: List<LaunchSummary>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String?,
        cachedLaunchStatus: String?
    )

    suspend fun getTotalEntries(): Int

    suspend fun deleteAll()

    suspend fun refreshPastLaunches(launches: List<LaunchSummary>)
}
