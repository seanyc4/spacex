package com.seancoyle.feature.launch.implementation.domain.network

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.data.network.dto.LaunchesDto
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchNetworkDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): Result<LaunchesDto, DataError>
}