package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyFromCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetCompanyFromCacheUseCase {

    override operator fun invoke(): Flow<DataResult<Company?>> = flow {
        emit(safeCacheCall(ioDispatcher) {
            cacheDataSource.getCompany()
        })

    }
}