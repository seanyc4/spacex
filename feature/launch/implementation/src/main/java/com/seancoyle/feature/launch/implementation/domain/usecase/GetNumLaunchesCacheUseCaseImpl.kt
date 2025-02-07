package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.data.cache.LaunchCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetNumLaunchesCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource
) : GetNumLaunchesCacheUseCase {

    override operator fun invoke(): Flow<Result<Int?, DataError>> = flow {
        emit(cacheDataSource.getTotalEntries())
    }
}