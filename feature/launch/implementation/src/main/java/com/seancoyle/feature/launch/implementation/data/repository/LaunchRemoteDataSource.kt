package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes

internal interface LaunchRemoteDataSource {

    suspend fun getLaunches(offset: Int): LaunchResult<List<LaunchTypes.Launch>, RemoteError>
}
