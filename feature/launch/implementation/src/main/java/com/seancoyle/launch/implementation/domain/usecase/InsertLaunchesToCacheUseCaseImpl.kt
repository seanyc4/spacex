package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.implementation.domain.cache.LaunchCacheDataSource
import javax.inject.Inject

internal class InsertLaunchesToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource
) : InsertLaunchesToCacheUseCase {

    override suspend operator fun invoke(launches: List<LaunchTypes.Launch>): Result<LongArray?, DataError> =
        cacheDataSource.insertList(launches)

}