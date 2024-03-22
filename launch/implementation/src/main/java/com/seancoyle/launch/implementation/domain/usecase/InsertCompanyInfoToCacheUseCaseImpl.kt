package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.CacheErrors.UNKNOWN_DATABASE_ERROR
import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class InsertCompanyInfoToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : InsertCompanyInfoToCacheUseCase {

    override suspend fun invoke(companyInfo: Company): Flow<DataResult<Unit>> = flow {
        val result = safeCacheCall(ioDispatcher) {
            cacheDataSource.insert(companyInfo)
        }

        when (result) {
            is DataResult.Success -> {
                emit(DataResult.Success(Unit))
            }

            is DataResult.Error -> {
                emit(DataResult.Error(result.exception))
            }

            else -> {
                emit(DataResult.Error(UNKNOWN_DATABASE_ERROR))
            }
        }
    }
}