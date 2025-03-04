package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchRemoteDataSource {
    suspend fun getLaunches(launchOptions: LaunchOptions): LaunchResult<List<LaunchTypes.Launch>, RemoteError>
}