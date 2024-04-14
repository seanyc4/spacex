package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.network.CompanyInfoNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyApiAndCacheUseCaseImpl @Inject constructor(
    private val insertCompanyInfoToCacheUseCase: InsertCompanyInfoToCacheUseCase,
    private val networkDataSource: CompanyInfoNetworkDataSource
) : GetCompanyApiAndCacheUseCase {

    override operator fun invoke(): Flow<Result<Company, DataError>> = flow {
        emit(getCompanyFromNetwork())
    }

    private suspend fun getCompanyFromNetwork(): Result<Company, DataError> {
        return when (val networkResult = networkDataSource.getCompany()) {
            is Result.Success -> cacheData(networkResult.data)
            is Result.Error -> Result.Error(networkResult.error)
        }
    }

    private suspend fun cacheData(company: Company): Result<Company, DataError> {
        return when (val cacheResult = insertCompanyInfoToCacheUseCase(company)) {
            is Result.Success -> Result.Success(company)
            is Result.Error -> Result.Error(cacheResult.error)
        }
    }
}