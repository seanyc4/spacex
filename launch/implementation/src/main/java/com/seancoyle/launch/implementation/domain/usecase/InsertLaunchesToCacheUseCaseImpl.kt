package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.DataResult.Companion.UNKNOWN_ERROR
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class InsertLaunchesToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : InsertLaunchesToCacheUseCase {
    override suspend fun invoke(launches: List<Launch>): Flow<DataResult<Unit>> = flow {
        val result = safeCacheCall(ioDispatcher) {
            cacheDataSource.insertList(launches)
        }

        when (result) {
            is DataResult.Success -> {
                emit(DataResult.Success(Unit))
            }

            is DataResult.Error -> {
                emit(DataResult.Error(result.exception))
            }

            else -> {
                emit(DataResult.Error(UNKNOWN_ERROR))
            }
        }
    }
}