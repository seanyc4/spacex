package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import kotlinx.coroutines.flow.Flow

internal interface GetNumLaunchesCacheUseCase {
    operator fun invoke(): Flow<LaunchResult<Int?, LocalError>>
}