package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetNumLaunchItemsFromCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource
) : GetNumLaunchItemsFromCacheUseCase {

    override operator fun invoke(): Flow<DataResult<Int?>> = flow {
        emit(cacheDataSource.getTotalEntries())
    }
}