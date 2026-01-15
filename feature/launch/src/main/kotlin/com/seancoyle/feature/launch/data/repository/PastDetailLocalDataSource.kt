package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.domain.model.Launch

internal interface PastDetailLocalDataSource {
    suspend fun getTotalEntries(): LaunchResult<Int, Throwable>

    suspend fun getLaunchDetail(id: String): LaunchResult<Launch?, Throwable>

    suspend fun upsertLaunchDetail(launch: Launch): LaunchResult<Unit, Throwable>

    suspend fun upsertAllLaunchDetails(launches: List<Launch>): LaunchResult<Unit, Throwable>

    suspend fun deleteAllLaunchDetails(): LaunchResult<Unit, Throwable>

    suspend fun refreshLaunches(launches: List<Launch>)
}
