package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.launch.api.domain.model.LaunchTypes

internal interface InsertLaunchesToCacheUseCase {
    suspend operator fun invoke(launches: List<LaunchTypes.Launch>): DataResult<LongArray?, DataError>
}