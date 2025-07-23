package com.seancoyle.feature.launch.implementation.data.local.launch

import com.seancoyle.core.common.crashlytics.Crashlytics
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.paginateLaunches
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_PAGE_SIZE
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.data.mapper.LaunchMapper
import com.seancoyle.feature.launch.implementation.data.mapper.LocalErrorMapper
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchLocalDataSource
import timber.log.Timber
import javax.inject.Inject

internal class LaunchLocalDataSourceImpl @Inject constructor(
    private val dao: LaunchDao,
    private val crashlytics: Crashlytics,
    private val localErrorMapper: LocalErrorMapper,
    private val launchMapper: LaunchMapper
) : LaunchLocalDataSource {

    override suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes.Launch>, LocalError> {
        return runCatching {
            val launchStatusEntity = launchMapper.mapToLaunchStatusEntity(launchStatus)
            val result = dao.paginateLaunches(
                launchYear = launchYear,
                launchStatus = launchStatusEntity,
                page = page,
                order = order,
                pageSize = PAGINATION_PAGE_SIZE
            )
            launchMapper.entityToDomainList(result)
        }.fold(
            onSuccess = { mappedResult ->
                LaunchResult.Success(mappedResult)
            },
            onFailure = {
                Timber.e(it)
                crashlytics.logException(it)
                LaunchResult.Error(localErrorMapper.map(it))
            }
        )
    }

    override suspend fun insert(launch: LaunchTypes.Launch): LaunchResult<Unit, LocalError> {
        return runCatching { dao.insert(launchMapper.domainToEntity(launch)) }
            .fold(
                onSuccess = { LaunchResult.Success(Unit) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    LaunchResult.Error(localErrorMapper.map(it))
                }
            )
    }

    override suspend fun insertList(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, LocalError> {
        return runCatching { dao.insertList(launchMapper.domainToEntityList(launches)) }
            .fold(
                onSuccess = { LaunchResult.Success(Unit) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    LaunchResult.Error(localErrorMapper.map(it))
                }
            )
    }

    override suspend fun deleteList(launches: List<LaunchTypes.Launch>): LaunchResult<Int, LocalError> {
        val ids = launches.map { it.id }
        return runCatching { dao.deleteList(ids) }
            .fold(
                onSuccess = { LaunchResult.Success(it) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    LaunchResult.Error(localErrorMapper.map(it))
                }
            )
    }

    override suspend fun deleteAll(): LaunchResult<Unit, LocalError> {
        return runCatching { dao.deleteAll() }
            .fold(
                onSuccess = { LaunchResult.Success(Unit) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    LaunchResult.Error(localErrorMapper.map(it))
                }
            )
    }

    override suspend fun deleteById(id: String): LaunchResult<Int, LocalError> {
        return runCatching { dao.deleteById(id) }
            .fold(
                onSuccess = { LaunchResult.Success(it) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    LaunchResult.Error(localErrorMapper.map(it))
                }
            )
    }

    override suspend fun getById(id: String): LaunchResult<LaunchTypes.Launch?, LocalError> {
        return runCatching { dao.getById(id) }
            .fold(
                onSuccess = { result ->
                    result?.let {
                        LaunchResult.Success(launchMapper.entityToDomain(it))
                    } ?: LaunchResult.Error(LocalError.CACHE_ERROR_NO_RESULTS)
                },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    LaunchResult.Error(localErrorMapper.map(it))
                }
            )
    }

    override suspend fun getAll(): LaunchResult<List<LaunchTypes.Launch>, LocalError> {
        return runCatching { dao.getAll() }
            .fold(
                onSuccess = { result ->
                    result?.let {
                        LaunchResult.Success(launchMapper.entityToDomainList(it))
                    } ?: LaunchResult.Error(LocalError.CACHE_ERROR_NO_RESULTS)
                },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    LaunchResult.Error(localErrorMapper.map(it))
                }
            )
    }

    override suspend fun getTotalEntries(): LaunchResult<Int, LocalError> {
        return runCatching { dao.getTotalEntries() }
            .fold(
                onSuccess = { LaunchResult.Success(it) },
                onFailure = {
                    Timber.e(it)
                    crashlytics.logException(it)
                    LaunchResult.Error(localErrorMapper.map(it))
                }
            )
    }

}
