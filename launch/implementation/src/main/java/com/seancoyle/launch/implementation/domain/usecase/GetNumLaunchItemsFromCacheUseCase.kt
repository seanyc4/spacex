package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import kotlinx.coroutines.flow.Flow

internal interface GetNumLaunchItemsFromCacheUseCase {
    operator fun invoke(): Flow<DataResult<Int?, DataError>>
}