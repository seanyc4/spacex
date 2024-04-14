package com.seancoyle.launch.implementation.domain.network

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.implementation.domain.model.LaunchOptions

internal interface LaunchNetworkDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError>
}