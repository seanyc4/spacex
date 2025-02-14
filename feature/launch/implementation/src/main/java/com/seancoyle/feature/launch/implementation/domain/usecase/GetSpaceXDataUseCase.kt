package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import kotlinx.coroutines.flow.Flow

internal interface GetSpaceXDataUseCase {
    operator fun invoke(): Flow<LaunchResult<Unit, DataSourceError>>
}