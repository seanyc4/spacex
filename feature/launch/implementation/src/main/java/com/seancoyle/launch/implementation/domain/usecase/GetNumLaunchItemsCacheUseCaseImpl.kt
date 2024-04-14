package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetNumLaunchItemsCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource
) : GetNumLaunchItemsCacheUseCase {

    override operator fun invoke(): Flow<Result<Int?, DataError>> = flow {
        emit(cacheDataSource.getTotalEntries())
    }
}