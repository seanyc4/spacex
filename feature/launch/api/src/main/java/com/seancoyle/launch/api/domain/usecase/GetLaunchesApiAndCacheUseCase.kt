package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.api.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

interface GetLaunchesApiAndCacheUseCase {
    operator fun invoke(): Flow<Result<List<LaunchTypes.Launch>, DataError>>
}