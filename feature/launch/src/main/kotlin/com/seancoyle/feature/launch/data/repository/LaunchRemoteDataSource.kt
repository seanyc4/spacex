package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchQuery

internal interface LaunchRemoteDataSource {

    suspend fun getLaunches(
        page: Int,
        launchQuery: LaunchQuery
    ): LaunchResult<List<Launch>, Throwable>
}
