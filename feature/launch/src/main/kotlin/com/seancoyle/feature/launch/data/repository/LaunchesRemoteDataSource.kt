package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchesQuery

internal interface LaunchesRemoteDataSource {

    suspend fun getLaunches(
        page: Int,
        launchesQuery: LaunchesQuery
    ): LaunchResult<List<Launch>, Throwable>
}
