package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.cache.CacheResult
import com.seancoyle.core.data.network.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyInfoFromCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetCompanyInfoFromCacheUseCase {

    override operator fun invoke(): Flow<CacheResult<Company?>> = flow {
        val result = safeCacheCall(ioDispatcher) {
            cacheDataSource.getCompany()
        }

        when(result) {
            is CacheResult.Success -> {
               result.data?.let { companyInfo ->
                   emit(CacheResult.Success(companyInfo))
               }
            }
            is CacheResult.Error -> {
                emit(result)
            }

            else -> { }
        }
    }

}