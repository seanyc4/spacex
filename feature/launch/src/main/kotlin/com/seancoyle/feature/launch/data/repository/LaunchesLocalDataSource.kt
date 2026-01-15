package com.seancoyle.feature.launch.data.repository

import com.seancoyle.feature.launch.domain.model.LaunchSummary

internal interface LaunchesLocalDataSource<RemoteKey : Any> {

    suspend fun getRemoteKeys(): List<RemoteKey?>

    suspend fun getRemoteKey(id: String): RemoteKey?

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

    suspend fun refreshLaunches(launches: List<LaunchSummary>)
}
