package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
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

    override suspend fun insertLaunchesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, DataSourceError> {
        return when (val result = launchDiskDataSource.insertList(launchDomainEntityMapper.domainToEntityList(launches))) {
            is LaunchResult.Success -> LaunchResult.Success(Unit)
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    override suspend fun getLaunchesApi(launchOptions: LaunchOptions): LaunchResult<List<LaunchTypes.Launch>, DataSourceError> {
        return when (val result = launchNetworkDataSource.getLaunches(launchOptions)) {
            is LaunchResult.Success -> LaunchResult.Success(launchDtoToDomainMapper.dtoToDomainList(result.data))
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    override suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes>, DataSourceError> {
        val launchStatusEntity = launchDomainEntityMapper.mapToLaunchStatusEntity(launchStatus)
        return when (val result = launchDiskDataSource.paginate(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatusEntity,
            page = page
        )) {
            is LaunchResult.Success -> LaunchResult.Success(launchDomainEntityMapper.entityToDomainList(result.data))
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    override suspend fun deleteLaunhesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Int, DataSourceError> {
        return launchDiskDataSource.deleteList(launches.map { launchDomainEntityMapper.domainToEntity(it) })
    }

    override suspend fun deleteAllCache(): LaunchResult<Unit, DataSourceError> {
        return launchDiskDataSource.deleteAll()
    }

    override suspend fun deleteByIdCache(id: String): LaunchResult<Int, DataSourceError> {
        return launchDiskDataSource.deleteById(id)
    }

    override suspend fun getByIdCache(id: String): LaunchResult<LaunchTypes.Launch?, DataSourceError> {
        return when (val result = launchDiskDataSource.getById(id)) {
            is LaunchResult.Success -> LaunchResult.Success(result.data?.let { launchDomainEntityMapper.entityToDomain(it) })
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    override suspend fun getAllCache(): LaunchResult<List<LaunchTypes>, DataSourceError> {
        return when (val result = launchDiskDataSource.getAll()) {
            is LaunchResult.Success -> LaunchResult.Success(launchDomainEntityMapper.entityToDomainList(result.data))
            is LaunchResult.Error -> LaunchResult.Error(result.error)
        }
    }

    override suspend fun getTotalEntriesCache(): LaunchResult<Int, DataSourceError> {
        return launchDiskDataSource.getTotalEntries()
    }

}