package com.seancoyle.launch.implementation.domain.network

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.domain.model.LaunchOptions

internal interface LaunchNetworkDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): DataResult<List<Launch>>
}