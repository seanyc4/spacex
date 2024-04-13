package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.domain.network.CompanyInfoNetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyApiAndCacheUseCaseImpl @Inject constructor(
    private val insertCompanyInfoToCacheUseCase: InsertCompanyInfoToCacheUseCase,
    private val networkDataSource: CompanyInfoNetworkDataSource
) : GetCompanyApiAndCacheUseCase {

    override operator fun invoke(): Flow<DataResult<Company, DataError>> = flow {
        emit(getCompanyFromNetwork())
    }

    private suspend fun getCompanyFromNetwork(): DataResult<Company, DataError> {
        return when (val networkResult = networkDataSource.getCompany()) {
            is DataResult.Success -> cacheData(networkResult.data)
            is DataResult.Error -> DataResult.Error(networkResult.error)
        }
    }

    private suspend fun cacheData(company: Company): DataResult<Company, DataError> {
        return when (val cacheResult = insertCompanyInfoToCacheUseCase(company)) {
            is DataResult.Success -> DataResult.Success(company)
            is DataResult.Error -> DataResult.Error(cacheResult.error)
        }
    }
}