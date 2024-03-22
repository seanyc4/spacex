package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Launch
import kotlinx.coroutines.flow.Flow

internal interface InsertLaunchesToCacheUseCase {
    suspend operator fun invoke(launches: List<Launch>): Flow<DataResult<Unit>>
}