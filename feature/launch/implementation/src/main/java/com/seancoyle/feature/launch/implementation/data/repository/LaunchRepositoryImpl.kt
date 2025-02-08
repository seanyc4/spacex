package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.cache.LaunchCacheDataSource
import com.seancoyle.feature.launch.implementation.data.cache.mapper.LaunchEntityMapper
import com.seancoyle.feature.launch.implementation.data.network.LaunchNetworkDataSource
import com.seancoyle.feature.launch.implementation.data.network.mapper.LaunchNetworkMapper
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import javax.inject.Inject

internal class LaunchRepositoryImpl @Inject constructor(
    private val launchNetworkDataSource: LaunchNetworkDataSource,
    private val launchCacheDataSource: LaunchCacheDataSource,
    private val launchNetworkMapper: LaunchNetworkMapper,
    private val launchCacheMapper: LaunchEntityMapper
): LaunchRepository {

    override suspend fun getLaunches(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError> {
        return when (val result = launchNetworkDataSource.getLaunches(launchOptions)) {
            is Result.Success -> Result.Success(launchNetworkMapper.dtoToDomainList(result.data))
            is Result.Error -> Result.Error(result.error)
        }
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

}