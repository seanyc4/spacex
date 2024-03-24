package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import kotlinx.coroutines.flow.Flow

internal interface GetNumLaunchItemsFromCacheUseCase {
    operator fun invoke(): Flow<DataResult<Int?>>
}