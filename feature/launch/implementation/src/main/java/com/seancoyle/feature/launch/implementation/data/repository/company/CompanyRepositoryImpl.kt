package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.cache.company.CompanyDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDto
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDtoEntityMapper
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import javax.inject.Inject

internal class CompanyRepositoryImpl @Inject constructor(
    private val companyNetworkDataSource: CompanyNetworkDataSource,
    private val companyDiskDataSource: CompanyDiskDataSource,
    private val companyDtoEntityMapper: CompanyDtoEntityMapper,
    private val companyCacheMapper: CompanyDomainEntityMapper
) : CompanyRepository {

    override suspend fun getCompanyApi(): Result<Company, DataError> {
        return when (val result = companyNetworkDataSource.getCompanyApi()) {
            is Result.Success -> insertCompanyIntoCache(result.data)
            is Result.Error -> Result.Error(result.error)
        }
    }

    private suspend fun insertCompanyIntoCache(companyDto: CompanyDto): Result<Company, DataError> {
        return when (val result = companyDiskDataSource.insert(companyDtoEntityMapper.dtoToEntity(companyDto))) {
            is Result.Success -> Result.Success(Company(
                employees = 8287,
                founded = 1555,
                founder = "regione",
                launchSites = 9871,
                name = "Jean Chase",
                valuation = 4965
            ))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getCompanyCache(): Result<Company?, DataError> {
        return when (val result = companyDiskDataSource.get()) {
            is Result.Success -> Result.Success(result.data?.let { companyCacheMapper.entityToDomain(it) })
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun deleteAllCompanyCache(): Result<Unit, DataError> {
        return companyDiskDataSource.deleteAll()
    }

}