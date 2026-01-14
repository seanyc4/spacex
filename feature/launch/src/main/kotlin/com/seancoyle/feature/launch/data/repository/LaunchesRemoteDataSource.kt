package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery

internal data class DetailedLaunchesResult(
    val summaries: List<LaunchSummary>,
    val details: List<Launch>
)

internal interface LaunchesRemoteDataSource {

    suspend fun getLaunches(
        page: Int,
        launchesQuery: LaunchesQuery
    ): LaunchResult<List<LaunchSummary>, Throwable>

    suspend fun getDetailedLaunches(
        page: Int,
        launchesQuery: LaunchesQuery
    ): LaunchResult<DetailedLaunchesResult, Throwable>

    suspend fun getLaunch(
        id: String,
        launchType: LaunchesType
    ): LaunchResult<Launch, RemoteError>
}
