package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchesDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchRemoteDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): LaunchResult<LaunchesDto, RemoteError>
}