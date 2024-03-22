package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.CacheErrors.UNKNOWN_DATABASE_ERROR
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

    override operator fun invoke(): Flow<DataResult<Int>> = flow {
        val result = safeCacheCall(ioDispatcher) {
            cacheDataSource.getTotalEntries()
        }

        when (result) {
            is DataResult.Success -> {
                result.data?.let { numLaunchItems ->
                    emit(DataResult.Success(numLaunchItems))
                }
            }

            is DataResult.Error -> {
                emit(result)
            }

            else -> {
                emit(DataResult.Error(UNKNOWN_DATABASE_ERROR))
            }
        }
    }

}