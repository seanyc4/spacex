package com.seancoyle.launch.implementation.domain.network

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.implementation.domain.model.LaunchOptions

internal interface LaunchNetworkDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): DataResult<List<LaunchTypes.Launch>, DataError>
}