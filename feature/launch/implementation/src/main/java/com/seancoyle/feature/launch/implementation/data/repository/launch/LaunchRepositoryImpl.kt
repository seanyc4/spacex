package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.cache.launch.LaunchDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchDtoDomainMapper
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import javax.inject.Inject

internal class LaunchRepositoryImpl @Inject constructor(
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchDiskDataSource: LaunchDiskDataSource,
    private val launchDtoToDomainMapper: LaunchDtoDomainMapper,
    private val launchDomainEntityMapper: LaunchDomainEntityMapper
): LaunchRepository {

    override suspend fun insertLaunchesCache(launches: List<LaunchTypes.Launch>): Result<Unit, DataError> {
        return when (val result = launchDiskDataSource.insertList(launchDomainEntityMapper.domainToEntityList(launches))) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getLaunchesApi(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError> {
        return when (val result = launchNetworkDataSource.getLaunches(launchOptions)) {
            is Result.Success -> Result.Success(launchDtoToDomainMapper.dtoToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Result<List<LaunchTypes>, DataError> {
        val launchStatusEntity = launchDomainEntityMapper.mapToLaunchStatusEntity(launchStatus)
        return when (val result = launchDiskDataSource.paginate(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatusEntity,
            page = page
        )) {
            is Result.Success -> Result.Success(launchDomainEntityMapper.entityToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun deleteLaunhesCache(launches: List<LaunchTypes.Launch>): Result<Int, DataError> {
        return launchDiskDataSource.deleteList(launches.map { launchDomainEntityMapper.domainToEntity(it) })
    }

    override suspend fun deleteAllCache(): Result<Unit, DataError> {
        return launchDiskDataSource.deleteAll()
    }

    override suspend fun deleteByIdCache(id: String): Result<Int, DataError> {
        return launchDiskDataSource.deleteById(id)
    }

    override suspend fun getByIdCache(id: String): Result<LaunchTypes.Launch?, DataError> {
        return when (val result = launchDiskDataSource.getById(id)) {
            is Result.Success -> Result.Success(result.data?.let { launchDomainEntityMapper.entityToDomain(it) })
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getAllCache(): Result<List<LaunchTypes>, DataError> {
        return when (val result = launchDiskDataSource.getAll()) {
            is Result.Success -> Result.Success(launchDomainEntityMapper.entityToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getTotalEntriesCache(): Result<Int, DataError> {
        return launchDiskDataSource.getTotalEntries()
    }

}