package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchesDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchNetworkDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): Result<LaunchesDto, DataError>
}