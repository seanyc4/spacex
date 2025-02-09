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
    private val launchCacheMapper: LaunchDomainEntityMapper
): LaunchRepository {

    override suspend fun insertList(launches: List<LaunchTypes.Launch>): Result<Unit, DataError> {
        return launchDiskDataSource.insertList(launchCacheMapper.domainToEntityList(launches))
    }

    override suspend fun getLaunches(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError> {
        return when (val result = launchNetworkDataSource.getLaunches(launchOptions)) {
            is Result.Success -> Result.Success(launchDtoToDomainMapper.dtoToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun paginateLaunches(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Result<List<LaunchTypes>, DataError> {
        val launchStatusEntity = launchCacheMapper.mapToLaunchStatusEntity(launchStatus)
        return when (val result = launchDiskDataSource.paginate(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatusEntity,
            page = page
        )) {
            is Result.Success -> Result.Success(launchCacheMapper.entityToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun deleteList(launches: List<LaunchTypes.Launch>): Result<Int, DataError> {
        return launchDiskDataSource.deleteList(launches.map { launchCacheMapper.domainToEntity(it) })
    }

    override suspend fun deleteAll(): Result<Unit, DataError> {
        return launchDiskDataSource.deleteAll()
    }

    override suspend fun deleteById(id: String): Result<Int, DataError> {
        return launchDiskDataSource.deleteById(id)
    }

    override suspend fun getById(id: String): Result<LaunchTypes.Launch?, DataError> {
        return when (val result = launchDiskDataSource.getById(id)) {
            is Result.Success -> Result.Success(result.data?.let { launchCacheMapper.entityToDomain(it) })
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getAll(): Result<List<LaunchTypes>, DataError> {
        return when (val result = launchDiskDataSource.getAll()) {
            is Result.Success -> Result.Success(launchCacheMapper.entityToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun getTotalEntries(): Result<Int, DataError> {
        return launchDiskDataSource.getTotalEntries()
    }

}