package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchesDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchRemoteDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): Result<LaunchesDto>
}