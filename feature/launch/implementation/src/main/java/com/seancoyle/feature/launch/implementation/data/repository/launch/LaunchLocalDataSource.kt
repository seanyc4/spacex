package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError.LocalError
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
    ): LaunchResult<List<LaunchEntity>, LocalError>

    suspend fun insert(launch: LaunchEntity): LaunchResult<Unit, LocalError>

    suspend fun deleteById(id: String): LaunchResult<Int, LocalError>

    suspend fun deleteList(launches: List<LaunchEntity>): LaunchResult<Int, LocalError>

    suspend fun deleteAll(): LaunchResult<Unit, LocalError>

    suspend fun getById(id: String): LaunchResult<LaunchEntity?, LocalError>

    suspend fun getAll(): LaunchResult<List<LaunchEntity>, LocalError>

    suspend fun getTotalEntries(): LaunchResult<Int, LocalError>

    suspend fun insertList(launches: List<LaunchEntity>): LaunchResult<Unit, LocalError>

}