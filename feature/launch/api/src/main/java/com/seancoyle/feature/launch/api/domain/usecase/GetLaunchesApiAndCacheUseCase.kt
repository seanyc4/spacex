package com.seancoyle.feature.launch.api.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import kotlinx.coroutines.flow.Flow

interface GetLaunchesApiAndCacheUseCase {
    operator fun invoke(): Flow<Result<Unit, DataError>>
}