package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import kotlinx.coroutines.flow.Flow

internal interface GetNumLaunchItemsCacheUseCase {
    operator fun invoke(): Flow<DataResult<Int?, DataError>>
}