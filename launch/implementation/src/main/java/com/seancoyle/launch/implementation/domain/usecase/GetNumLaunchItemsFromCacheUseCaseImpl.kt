package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetNumLaunchItemsFromCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetNumLaunchItemsFromCacheUseCase {

    override operator fun invoke(): Flow<DataResult<Int?>> = flow {
        emit(safeCacheCall(ioDispatcher) {
            cacheDataSource.getTotalEntries()
        })

    }
}