package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes

internal interface InsertLaunchesToCacheUseCase {
    suspend operator fun invoke(launches: List<LaunchTypes.Launch>): Result<LongArray?, DataError>
}