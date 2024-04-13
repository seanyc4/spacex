package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.launch.api.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

internal interface GetLaunchesApiAndCacheUseCase {
    operator fun invoke(): Flow<DataResult<List<LaunchTypes.Launch>, DataError>>
}