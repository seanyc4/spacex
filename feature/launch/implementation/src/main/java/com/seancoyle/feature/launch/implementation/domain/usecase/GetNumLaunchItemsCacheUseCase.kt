package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import kotlinx.coroutines.flow.Flow

internal interface GetNumLaunchItemsCacheUseCase {
    operator fun invoke(): Flow<Result<Int?, DataError>>
}