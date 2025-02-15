package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.cache.company.LocalErrorMapper
import com.seancoyle.feature.launch.implementation.data.cache.launch.LaunchDomainEntityMapper
import com.seancoyle.feature.launch.implementation.data.cache.launch.RemoteErrorMapper
import com.seancoyle.feature.launch.implementation.data.network.launch.LaunchDtoDomainMapper
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import javax.inject.Inject

internal class LaunchRepositoryImpl @Inject constructor(
    private val launchRemoteDataSource: LaunchRemoteDataSource,
    private val launchLocalDataSource: LaunchLocalDataSource,
    private val launchDtoToDomainMapper: LaunchDtoDomainMapper,
    private val launchDomainEntityMapper: LaunchDomainEntityMapper,
    private val remoteErrorMapper: RemoteErrorMapper,
    private val localErrorMapper: LocalErrorMapper
): LaunchRepository {

    override suspend fun insertLaunchesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, LocalError> {
        return launchLocalDataSource.insertList(launchDomainEntityMapper.domainToEntityList(launches)).fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = { LaunchResult.Error(localErrorMapper.map(it)) }
        )
    }

    override suspend fun getLaunchesApi(launchOptions: LaunchOptions): LaunchResult<List<LaunchTypes.Launch>, RemoteError> {
        return launchRemoteDataSource.getLaunches(launchOptions).fold(
            onSuccess = { LaunchResult.Success(launchDtoToDomainMapper.dtoToDomainList(it)) },
            onFailure = { LaunchResult.Error(remoteErrorMapper.map(it)) }
        )
    }

    override suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes>, LocalError> {
        val launchStatusEntity = launchDomainEntityMapper.mapToLaunchStatusEntity(launchStatus)
        return launchLocalDataSource.paginate(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatusEntity,
            page = page
        ).fold(
            onSuccess = { LaunchResult.Success(launchDomainEntityMapper.entityToDomainList(it)) },
            onFailure = { LaunchResult.Error(localErrorMapper.map(it)) }
        )
    }

    override suspend fun deleteLaunhesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Int, LocalError> {
        return launchLocalDataSource.deleteList(launches.map { launchDomainEntityMapper.domainToEntity(it) }).fold(
            onSuccess = { LaunchResult.Success(it) },
            onFailure = { LaunchResult.Error(localErrorMapper.map(it)) }
        )
    }

    override suspend fun deleteAllCache(): LaunchResult<Unit, LocalError> {
        return launchLocalDataSource.deleteAll().fold(
            onSuccess = { LaunchResult.Success(Unit) },
            onFailure = { LaunchResult.Error(localErrorMapper.map(it)) }
        )
    }

    override suspend fun deleteByIdCache(id: String): LaunchResult<Int, LocalError> {
        return launchLocalDataSource.deleteById(id).fold(
            onSuccess = { LaunchResult.Success(it) },
            onFailure = { LaunchResult.Error(localErrorMapper.map(it)) }
        )
    }

    override suspend fun getByIdCache(id: String): LaunchResult<LaunchTypes.Launch?, LocalError> {
        return launchLocalDataSource.getById(id).fold(
            onSuccess = { LaunchResult.Success(it?.let { launchDomainEntityMapper.entityToDomain(it) }) },
            onFailure = { LaunchResult.Error(localErrorMapper.map(it)) }
        )
    }

    override suspend fun getAllCache(): LaunchResult<List<LaunchTypes>, LocalError> {
        return launchLocalDataSource.getAll().fold(
            onSuccess = { LaunchResult.Success(launchDomainEntityMapper.entityToDomainList(it)) },
            onFailure = { LaunchResult.Error(localErrorMapper.map(it)) }
        )
    }

    override suspend fun getTotalEntriesCache(): LaunchResult<Int, LocalError> {
        return launchLocalDataSource.getTotalEntries().fold(
            onSuccess = { LaunchResult.Success(it) },
            onFailure = { LaunchResult.Error(localErrorMapper.map(it)) }
        )
    }

}