package com.seancoyle.feature.launch.data.repository

import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.model.LaunchTypes

internal interface LaunchRemoteDataSource {

    suspend fun getLaunches(
        page: Int,
        launchQuery: LaunchQuery
    ): LaunchResult<List<LaunchTypes.Launch>, Throwable>
}
