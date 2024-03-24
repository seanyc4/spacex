package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.CacheErrors.UNKNOWN_DATABASE_ERROR
import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.NetworkErrors.NETWORK_DATA_NULL
import com.seancoyle.core.data.NetworkErrors.UNKNOWN_NETWORK_ERROR
import com.seancoyle.core.data.safeApiCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyApiAndCacheUseCaseImpl @Inject constructor(
    private val insertCompanyInfoToCacheUseCase: InsertCompanyInfoToCacheUseCase,
    private val networkDataSource: CompanyInfoNetworkDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : GetCompanyApiAndCacheUseCase {

    override operator fun invoke(): Flow<DataResult<Company>> = flow {
        val result = safeApiCall(ioDispatcher) {
            networkDataSource.getCompany()
        }

        when (result) {
            is DataResult.Success -> {
                result.data?.let { companyInfo ->
                    when (val insertResult = insertCompanyInfoToCacheUseCase(companyInfo)) {
                        is DataResult.Success -> {
                            emit(DataResult.Success(companyInfo))
                        }

                        is DataResult.Error -> {
                            emit(DataResult.Error(insertResult.exception))
                        }

                        else -> {
                            emit(DataResult.Error(UNKNOWN_DATABASE_ERROR))
                        }
                    }
                } ?: emit(DataResult.Error(NETWORK_DATA_NULL))
            }

            is DataResult.Error -> {
                emit(DataResult.Error(result.exception))
            }

            else -> {
                emit(DataResult.Error(UNKNOWN_NETWORK_ERROR))
            }
        }
    }
}