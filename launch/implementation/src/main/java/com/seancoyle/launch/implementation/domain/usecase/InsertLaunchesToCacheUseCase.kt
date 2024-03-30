package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.Launch

internal interface InsertLaunchesToCacheUseCase {
    suspend operator fun invoke(launches: List<Launch>): DataResult<LongArray?, DataError>
}