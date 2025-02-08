package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.api.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.cache.CompanyCacheDataSource
import com.seancoyle.feature.launch.implementation.data.cache.LaunchCacheDataSource
import com.seancoyle.feature.launch.implementation.data.network.mapper.CompanyNetworkMapper
import com.seancoyle.feature.launch.implementation.data.network.mapper.LaunchNetworkMapper
import com.seancoyle.feature.launch.implementation.data.cache.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.implementation.data.cache.mapper.CompanyEntityMapper
import com.seancoyle.feature.launch.implementation.data.cache.mapper.LaunchEntityMapper
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.data.network.CompanyNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.network.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import javax.inject.Inject

internal class SpaceXRepositoryImpl @Inject constructor(
    private val companyNetworkDataSource: CompanyNetworkDataSource,
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchCacheDataSource: LaunchCacheDataSource,
    private val companyCacheDataSource: CompanyCacheDataSource,
    private val launchPreferencesDataSource: LaunchPreferencesDataSource,
    private val companyNetworkMapper: CompanyNetworkMapper,
    private val launchNetworkMapper: LaunchNetworkMapper,
    private val companyCacheMapper: CompanyEntityMapper,
    private val launchCacheMapper: LaunchEntityMapper,
) : SpaceXRepository {

    override suspend fun getCompany(): Result<Company, DataError> {
        return when (val result = companyNetworkDataSource.getCompany()) {
            is Result.Success -> Result.Success(companyNetworkMapper.dtoToDomain(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getLaunches(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError> {
        return when (val result = launchNetworkDataSource.getLaunches(launchOptions)) {
            is Result.Success -> Result.Success(launchNetworkMapper.dtoToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun saveLaunchPreferences(
        order: Order,
        launchStatus: LaunchStatus,
        launchYear: String
    ) {
        launchPreferencesDataSource.saveLaunchPreferences(
            order = order,
            launchStatus = launchStatus,
            launchYear = launchYear
        )
    }

    override suspend fun paginateLaunches(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Result<List<LaunchTypes>, DataError> {
        val launchStatusEntity = launchCacheMapper.toLaunchStatusEntity(launchStatus)
        return when (val result = launchCacheDataSource.paginateLaunches(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatusEntity,
            page = page
        )) {
            is Result.Success -> Result.Success(launchCacheMapper.entityToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getLaunchPreferences(): LaunchPrefs {
        return launchPreferencesDataSource.getLaunchPreferences()
    }

    override suspend fun insertLaunch(launch: LaunchTypes.Launch): Result<Long, DataError> {
        return launchCacheDataSource.insert(launchCacheMapper.domainToEntity(launch))
    }

    override suspend fun insertLaunches(launches: List<LaunchTypes.Launch>): Result<LongArray, DataError> {
        return launchCacheDataSource.insertList(launches.map { launchCacheMapper.domainToEntity(it) })
    }

    override suspend fun deleteList(launches: List<LaunchTypes.Launch>): Result<Int, DataError> {
        return launchCacheDataSource.deleteList(launches.map { launchCacheMapper.domainToEntity(it) })
    }

    override suspend fun deleteAll(): Result<Unit, DataError> {
        return launchCacheDataSource.deleteAll()
    }

    override suspend fun deleteById(id: String): Result<Int, DataError> {
        return launchCacheDataSource.deleteById(id)
    }

    override suspend fun getById(id: String): Result<LaunchTypes.Launch?, DataError> {
        return when (val result = launchCacheDataSource.getById(id)) {
            is Result.Success -> Result.Success(result.data?.let { launchCacheMapper.entityToDomain(it) })
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getAll(): Result<List<LaunchTypes>, DataError> {
        return when (val result = launchCacheDataSource.getAll()) {
            is Result.Success -> Result.Success(launchCacheMapper.entityToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getTotalEntries(): Result<Int, DataError> {
        return launchCacheDataSource.getTotalEntries()
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