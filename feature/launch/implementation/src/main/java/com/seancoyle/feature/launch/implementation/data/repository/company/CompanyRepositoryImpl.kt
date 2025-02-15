package com.seancoyle.feature.launch.implementation.data.repository.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.*
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.data.cache.company.CompanyDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.cache.company.LocalDataSourceErrorMapper
import com.seancoyle.feature.launch.implementation.data.cache.launch.RemoteDataSourceErrorMapper
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDto
import com.seancoyle.feature.launch.implementation.data.network.company.CompanyDtoEntityMapper
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import javax.inject.Inject

internal class CompanyRepositoryImpl @Inject constructor(
    private val companyRemoteDataSource: CompanyRemoteDataSource,
    private val companyLocalDataSource: CompanyLocalDataSource,
    private val companyDtoEntityMapper: CompanyDtoEntityMapper,
    private val companyCacheMapper: CompanyDomainEntityMapper,
    private val remoteDataSourceErrorMapper: RemoteDataSourceErrorMapper,
    private val localDataSourceErrorMapper: LocalDataSourceErrorMapper
) : CompanyRepository {

    override suspend fun getCompanyApi(): LaunchResult<Unit, DataError> {
        return companyRemoteDataSource.getCompanyApi().fold(
            onSuccess = { insertCompanyIntoCache(it) },
            onFailure = { LaunchResult.Error(remoteDataSourceErrorMapper.map(it)) }
        )
    }

    private suspend fun insertCompanyIntoCache(companyDto: CompanyDto): LaunchResult<Unit, LocalError> {
        return companyLocalDataSource.insert(companyDtoEntityMapper.dtoToEntity(companyDto)).fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = { LaunchResult.Error(localDataSourceErrorMapper.map(it)) }
        )
    }

    override suspend fun getCompanyCache(): LaunchResult<Company?, LocalError> {
        return companyLocalDataSource.get().fold(
            onSuccess = { LaunchResult.Success(it?.let { companyCacheMapper.entityToDomain(it) })},
            onFailure = { LaunchResult.Error(localDataSourceErrorMapper.map(it)) }
        )
    }

    override suspend fun deleteAllCompanyCache(): LaunchResult<Unit, LocalError> {
        return companyLocalDataSource.deleteAll().fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = { LaunchResult.Error(localDataSourceErrorMapper.map(it)) }
        )
    }

}