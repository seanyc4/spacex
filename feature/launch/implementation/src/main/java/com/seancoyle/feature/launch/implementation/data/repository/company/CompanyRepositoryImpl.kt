package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.*
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import javax.inject.Inject

internal class CompanyRepositoryImpl @Inject constructor(
    private val companyRemoteDataSource: CompanyRemoteDataSource,
    private val companyLocalDataSource: CompanyLocalDataSource
) : CompanyRepository {

    override suspend fun getCompanyApi(): LaunchResult<Unit, DataError> {
        return when (val result = companyRemoteDataSource.getCompanyApi()) {
            is LaunchResult.Success -> insertCompanyIntoCache(result.data)
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    private suspend fun insertCompanyIntoCache(company: Company): LaunchResult<Unit, LocalError> {
        return when (val result = companyLocalDataSource.insert(company)) {
            is LaunchResult.Success -> LaunchResult.Success(Unit)
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    override suspend fun getCompanyCache(): LaunchResult<Company, LocalError> {
        return companyLocalDataSource.get()
    }

    override suspend fun deleteAllCompanyCache(): LaunchResult<Unit, LocalError> {
        return companyLocalDataSource.deleteAll()
    }

}