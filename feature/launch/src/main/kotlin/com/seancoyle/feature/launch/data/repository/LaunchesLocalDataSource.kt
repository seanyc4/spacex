package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary

internal interface LaunchesLocalDataSource {

    suspend fun getRemoteKeys(): List<LaunchRemoteKeyEntity?>

    suspend fun getRemoteKey(id: String): LaunchRemoteKeyEntity?

    suspend fun refreshLaunches(launches: List<LaunchSummary>)

    suspend fun refreshLaunchesWithKeys(
        launches: List<LaunchSummary>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String? = null,
        cachedLaunchType: String? = null,
        cachedLaunchStatus: String? = null
    )

    suspend fun appendLaunchesWithKeys(
        launches: List<LaunchSummary>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int,
        cachedQuery: String? = null,
        cachedLaunchType: String? = null,
        cachedLaunchStatus: String? = null
    )

    suspend fun upsert(launch: LaunchSummary): LaunchResult<Unit, Throwable>

    suspend fun upsertAll(launches: List<LaunchSummary>): LaunchResult<Unit, Throwable>

    suspend fun deleteAll(): LaunchResult<Unit, Throwable>

    suspend fun getById(id: String): LaunchResult<LaunchSummary?, Throwable>

    suspend fun getTotalEntries(): LaunchResult<Int, Throwable>

    suspend fun getTotalEntriesByType(launchType: String): LaunchResult<Int, Throwable>

    suspend fun getLaunchDetail(id: String): LaunchResult<Launch?, Throwable>

    suspend fun upsertLaunchDetail(launch: Launch): LaunchResult<Unit, Throwable>

    suspend fun upsertAllLaunchDetails(launches: List<Launch>): LaunchResult<Unit, Throwable>

    suspend fun deleteAllLaunchDetails(): LaunchResult<Unit, Throwable>

}
