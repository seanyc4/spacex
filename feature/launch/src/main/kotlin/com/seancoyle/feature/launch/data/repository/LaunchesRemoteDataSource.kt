package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.model.DetailedLaunchesResult
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchesQuery

internal interface LaunchesRemoteDataSource {

    suspend fun getUpcomingDetailedLaunches(
        page: Int,
        launchesQuery: LaunchesQuery
    ): LaunchResult<DetailedLaunchesResult, Throwable>

    suspend fun getPastDetailedLaunches(
        page: Int,
        launchesQuery: LaunchesQuery
    ): LaunchResult<DetailedLaunchesResult, Throwable>

    suspend fun getLaunch(
        id: String,
        launchType: LaunchesType
    ): LaunchResult<Launch, RemoteError>
}
