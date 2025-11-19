package com.seancoyle.feature.launch.implementation.data.repository.launch

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes

internal interface LaunchLocalDataSource {

    suspend fun paginate(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes.Launch>, LocalError>

    suspend fun insert(launch: LaunchTypes.Launch): LaunchResult<Unit, LocalError>

    suspend fun deleteById(id: String): LaunchResult<Int, LocalError>

    suspend fun deleteList(launches: List<LaunchTypes.Launch>): LaunchResult<Int, LocalError>

    suspend fun deleteAll(): LaunchResult<Unit, LocalError>

    suspend fun getById(id: String): LaunchResult<LaunchTypes.Launch?, LocalError>

    suspend fun getAll(): LaunchResult<List<LaunchTypes.Launch>, LocalError>

    suspend fun getTotalEntries(): LaunchResult<Int, LocalError>

    suspend fun insertList(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, LocalError>
}
