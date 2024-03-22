package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.launch.api.domain.model.Launch
import kotlinx.coroutines.flow.Flow

internal interface GetLaunchesFromNetworkAndInsertToCacheUseCase {
    operator fun invoke(): Flow<ApiResult<List<Launch>>>
}