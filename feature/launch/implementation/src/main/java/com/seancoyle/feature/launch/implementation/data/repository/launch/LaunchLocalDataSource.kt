package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity

internal interface LaunchLocalDataSource {

    suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatusEntity,
        page: Int
    ): LaunchResult<List<LaunchEntity>, DataSourceError>

    suspend fun insert(launch: LaunchEntity): LaunchResult<Unit, DataSourceError>

    suspend fun deleteById(id: String): LaunchResult<Int, DataSourceError>

    suspend fun deleteList(launches: List<LaunchEntity>): LaunchResult<Int, DataSourceError>

    suspend fun deleteAll(): LaunchResult<Unit, DataSourceError>

    suspend fun getById(id: String): LaunchResult<LaunchEntity?, DataSourceError>

    suspend fun getAll(): LaunchResult<List<LaunchEntity>, DataSourceError>

    suspend fun getTotalEntries(): LaunchResult<Int, DataSourceError>

    suspend fun insertList(launches: List<LaunchEntity>): LaunchResult<Unit, DataSourceError>

}