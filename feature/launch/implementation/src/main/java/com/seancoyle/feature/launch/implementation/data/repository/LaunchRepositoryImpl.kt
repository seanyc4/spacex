package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LaunchRepositoryImpl @Inject constructor(
    private val launchRemoteDataSource: LaunchRemoteDataSource,
    private val launchLocalDataSource: LaunchLocalDataSource
): LaunchRepository {

    override fun observeAll(): Flow<LaunchResult<List<LaunchTypes.Launch>, LocalError>> {
        return launchLocalDataSource.observeAll()
    }

    override suspend fun insertLaunchesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, LocalError> {
        return launchLocalDataSource.insertList(launches)
    }

    override suspend fun getLaunchesApi(offset: Int): LaunchResult<List<LaunchTypes.Launch>, RemoteError> {
        return launchRemoteDataSource.getLaunches(offset)
    }

    override suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes>, LocalError> {
        return launchLocalDataSource.paginate(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatus,
            page = page
        )
    }

    override suspend fun deleteLaunhesCache(launches: List<LaunchTypes.Launch>): LaunchResult<Int, LocalError> {
        return launchLocalDataSource.deleteList(launches)
    }

    override suspend fun deleteAllCache(): LaunchResult<Unit, LocalError> {
        return launchLocalDataSource.deleteAll()
    }

    override suspend fun deleteByIdCache(id: String): LaunchResult<Int, LocalError> {
        return launchLocalDataSource.deleteById(id)
    }

    override suspend fun getByIdCache(id: String): LaunchResult<LaunchTypes.Launch?, LocalError> {
        return launchLocalDataSource.getById(id)
    }

    override suspend fun getAllCache(): LaunchResult<List<LaunchTypes.Launch>, LocalError> {
        return launchLocalDataSource.getAll()
    }

    override suspend fun getTotalEntriesCache(): LaunchResult<Int, LocalError> {
        return launchLocalDataSource.getTotalEntries()
    }
}
