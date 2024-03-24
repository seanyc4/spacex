package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class InsertLaunchesToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : InsertLaunchesToCacheUseCase {

    override suspend operator fun invoke(launches: List<Launch>): DataResult<LongArray?> =
        safeCacheCall(ioDispatcher) {
            cacheDataSource.insertList(launches)
        }

}