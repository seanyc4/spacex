package com.seancoyle.feature.launch.api.domain.usecase

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import kotlinx.coroutines.flow.Flow

interface GetLaunchesApiAndCacheUseCase {
    operator fun invoke(): Flow<LaunchResult<Unit, DataSourceError>>
}