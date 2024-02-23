package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetNumLaunchItemsFromCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource
) : GetNumLaunchItemsFromCacheUseCase {

    override operator fun invoke(): Flow<Int> = flow {
        cacheDataSource.getTotalEntries()
    }

}