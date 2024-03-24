package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Launch

internal interface InsertLaunchesToCacheUseCase {
    suspend operator fun invoke(launches: List<Launch>): DataResult<LongArray?>
}