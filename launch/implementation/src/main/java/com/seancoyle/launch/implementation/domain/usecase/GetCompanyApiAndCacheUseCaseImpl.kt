package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.CacheErrors.UNKNOWN_DATABASE_ERROR
import com.seancoyle.core.data.DataResult
import com.seancoyle.core.data.NetworkErrors.UNKNOWN_NETWORK_ERROR
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyApiAndCacheUseCaseImpl @Inject constructor(
    private val insertCompanyInfoToCacheUseCase: InsertCompanyInfoToCacheUseCase,
    private val networkDataSource: CompanyInfoNetworkDataSource
) : GetCompanyApiAndCacheUseCase {

    override operator fun invoke(): Flow<DataResult<Company>> = flow {
        when (val result = networkDataSource.getCompany()) {
            is DataResult.Success -> {
                result.data.let { companyInfo ->
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
                }
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