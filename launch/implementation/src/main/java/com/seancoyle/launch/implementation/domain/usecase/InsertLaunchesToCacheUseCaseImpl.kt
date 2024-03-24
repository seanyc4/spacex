package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSource
import javax.inject.Inject

internal class InsertLaunchesToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource
) : InsertLaunchesToCacheUseCase {

    override suspend operator fun invoke(launches: List<Launch>): DataResult<LongArray?> =
        cacheDataSource.insertList(launches)

}