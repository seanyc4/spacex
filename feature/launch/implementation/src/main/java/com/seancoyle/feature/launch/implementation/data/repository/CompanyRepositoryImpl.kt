package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.cache.CompanyCacheDataSource
import com.seancoyle.feature.launch.implementation.data.cache.mapper.CompanyDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.CompanyNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.network.mapper.CompanyDtoEntityMapper
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import javax.inject.Inject

internal class CompanyRepositoryImpl @Inject constructor(
    private val companyNetworkDataSource: CompanyNetworkDataSource,
    private val companyCacheDataSource: CompanyCacheDataSource,
    private val companyDtoEntityMapper: CompanyDtoEntityMapper,
    private val companyCacheMapper: CompanyDomainEntityMapper
): CompanyRepository {

    override suspend fun getCompany(): Result<Unit, DataError> {
        return when (val result = companyNetworkDataSource.getCompany()) {
            is Result.Success -> {
                companyCacheDataSource.insert(companyDtoEntityMapper.dtoToEntity(result.data))
                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
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