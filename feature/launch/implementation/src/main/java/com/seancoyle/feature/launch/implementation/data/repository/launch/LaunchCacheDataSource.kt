package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity

internal interface LaunchCacheDataSource {

    suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatusEntity,
        page: Int
    ): Result<List<LaunchEntity>, DataError>

    suspend fun insert(launch: LaunchEntity): Result<Unit, DataError>

    suspend fun deleteById(id: String): Result<Int, DataError>

    suspend fun deleteList(launches: List<LaunchEntity>): Result<Int, DataError>

    suspend fun deleteAll(): Result<Unit, DataError>

    suspend fun getById(id: String): Result<LaunchEntity?, DataError>

    suspend fun getAll(): Result<List<LaunchEntity>, DataError>

    suspend fun getTotalEntries(): Result<Int, DataError>

    suspend fun insertList(launches: List<LaunchEntity>): Result<Unit, DataError>

}