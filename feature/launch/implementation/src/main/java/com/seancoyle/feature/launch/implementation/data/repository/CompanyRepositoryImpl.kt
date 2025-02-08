package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.cache.CompanyCacheDataSource
import com.seancoyle.feature.launch.implementation.data.cache.mapper.CompanyEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.CompanyNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.network.mapper.CompanyNetworkMapper
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import javax.inject.Inject

internal class CompanyRepositoryImpl @Inject constructor(
    private val companyNetworkDataSource: CompanyNetworkDataSource,
    private val companyCacheDataSource: CompanyCacheDataSource,
    private val companyNetworkMapper: CompanyNetworkMapper,
    private val companyCacheMapper: CompanyEntityMapper
): CompanyRepository {

    override suspend fun getCompany(): Result<Company, DataError> {
        return when (val result = companyNetworkDataSource.getCompany()) {
            is Result.Success -> Result.Success(companyNetworkMapper.dtoToDomain(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun insertCompany(company: Company): Result<Long, DataError> {
        return companyCacheDataSource.insert(companyCacheMapper.domainToEntity(company))
    }

    override suspend fun getCompanyFromCache(): Result<Company?, DataError> {
        return when (val result = companyCacheDataSource.getCompany()) {
            is Result.Success -> Result.Success(result.data?.let { companyCacheMapper.entityToDomain(it) })
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun deleteAllCompanyCache(): Result<Unit, DataError> {
        return companyCacheDataSource.deleteAll()
    }

}