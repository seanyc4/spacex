package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchesDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchRemoteDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): LaunchResult<LaunchesDto, DataSourceError>
}