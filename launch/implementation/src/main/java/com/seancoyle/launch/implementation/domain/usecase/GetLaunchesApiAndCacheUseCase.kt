package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.Launch
import kotlinx.coroutines.flow.Flow

internal interface GetLaunchesApiAndCacheUseCase {
    operator fun invoke(): Flow<DataResult<List<Launch>, DataError>>
}