package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.core.data.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSource
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource,
    private val networkDataSource: CompanyInfoNetworkDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetCompanyInfoFromNetworkAndInsertToCacheUseCase {

    override operator fun invoke(): Flow<DataResult<Company>> = flow {
        val result = safeApiCall(ioDispatcher) {
            networkDataSource.getCompany()
        }

        when (result) {
            is DataResult.Success -> {
                result.data?.let { companyInfo ->
                    safeCacheCall(ioDispatcher) {
                        cacheDataSource.insert(companyInfo)
                    }
                    emit(DataResult.Success(companyInfo))
                }
            }

            is DataResult.Error -> {
                emit(result)
            }

            else -> {}
        }
    }
}