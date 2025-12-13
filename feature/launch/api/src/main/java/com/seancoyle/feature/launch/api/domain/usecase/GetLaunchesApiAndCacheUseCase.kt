package com.seancoyle.feature.launch.api.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import kotlinx.coroutines.flow.Flow

interface GetLaunchesApiAndCacheUseCase {
    operator fun invoke(currentPage: Int): Flow<LaunchResult<Unit, DataError>>
}

