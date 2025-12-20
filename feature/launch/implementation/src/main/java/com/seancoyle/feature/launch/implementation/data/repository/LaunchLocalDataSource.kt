package com.seancoyle.feature.launch.implementation.data.repository

import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes

internal interface LaunchLocalDataSource {

    suspend fun getRemoteKeys(): List<LaunchRemoteKeyEntity?>

    suspend fun getRemoteKey(id: String): LaunchRemoteKeyEntity?

    suspend fun getRemoteKeyCreationTime(id: String): Long?

    suspend fun refreshLaunches(launches: List<LaunchTypes.Launch>)

    suspend fun refreshLaunchesWithKeys(
        launches: List<LaunchTypes.Launch>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int
    )

    suspend fun appendLaunchesWithKeys(
        launches: List<LaunchTypes.Launch>,
        nextPage: Int?,
        prevPage: Int?,
        currentPage: Int
    )

    suspend fun upsert(launch: LaunchTypes.Launch): LaunchResult<Unit, Throwable>

    suspend fun upsertAll(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, Throwable>

    suspend fun deleteAll(): LaunchResult<Unit, Throwable>

    suspend fun getById(id: String): LaunchResult<LaunchTypes.Launch?, Throwable>

    suspend fun getTotalEntries(): LaunchResult<Int, Throwable>

    suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes.Launch>, Throwable>
}
