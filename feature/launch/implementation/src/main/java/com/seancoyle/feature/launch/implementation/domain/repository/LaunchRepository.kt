package com.seancoyle.feature.launch.implementation.domain.repository

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions

internal interface LaunchRepository {
    suspend fun getLaunches(launchOptions: LaunchOptions): Result<List<LaunchTypes.Launch>, DataError>
    suspend fun insertLaunch(launch: LaunchTypes.Launch): Result<Long, DataError>
    suspend fun insertLaunches(launches: List<LaunchTypes.Launch>): Result<LongArray, DataError>
    suspend fun deleteList(launches: List<LaunchTypes.Launch>): Result<Int, DataError>
    suspend fun deleteAll(): Result<Unit, DataError>
    suspend fun deleteById(id: String): Result<Int, DataError>
    suspend fun getById(id: String): Result<LaunchTypes.Launch?, DataError>
    suspend fun getAll(): Result<List<LaunchTypes>, DataError>
    suspend fun getTotalEntries(): Result<Int, DataError>
    suspend fun paginateLaunches(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): Result<List<LaunchTypes>, DataError>
}