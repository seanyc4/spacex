package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchStatusEntity

internal interface LaunchLocalDataSource {

    suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatusEntity,
        page: Int
    ): Result<List<LaunchEntity>>

    suspend fun insert(launch: LaunchEntity): Result<Unit>

    suspend fun deleteById(id: String): Result<Int>

    suspend fun deleteList(launches: List<LaunchEntity>): Result<Int>

    suspend fun deleteAll(): Result<Unit>

    suspend fun getById(id: String): Result<LaunchEntity?>

    suspend fun getAll(): Result<List<LaunchEntity>>

    suspend fun getTotalEntries(): Result<Int>

    suspend fun insertList(launches: List<LaunchEntity>): Result<Unit>

}