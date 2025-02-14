package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.cache.company.CompanyDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDto
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDtoEntityMapper
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import javax.inject.Inject

internal class CompanyRepositoryImpl @Inject constructor(
    private val companyNetworkDataSource: CompanyNetworkDataSource,
    private val companyLocalDataSource: CompanyLocalDataSource,
    private val companyDtoEntityMapper: CompanyDtoEntityMapper,
    private val companyCacheMapper: CompanyDomainEntityMapper
) : CompanyRepository {

    override suspend fun getCompanyApi(): LaunchResult<Unit, DataSourceError> {
        return when (val result = companyNetworkDataSource.getCompanyApi()) {
            is LaunchResult.Success -> insertCompanyIntoCache(result.data)
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    private suspend fun insertCompanyIntoCache(companyDto: CompanyDto): LaunchResult<Unit, DataSourceError> {
        return when (val result = companyLocalDataSource.insert(companyDtoEntityMapper.dtoToEntity(companyDto))) {
            is LaunchResult.Success -> LaunchResult.Success(Unit)
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    override suspend fun getCompanyCache(): LaunchResult<Company?, DataSourceError> {
        return when (val result = companyLocalDataSource.get()) {
            is LaunchResult.Success -> LaunchResult.Success(result.data?.let { companyCacheMapper.entityToDomain(it) })
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    override suspend fun deleteAllCompanyCache(): LaunchResult<Unit, DataSourceError> {
        return companyLocalDataSource.deleteAll()
    }

}