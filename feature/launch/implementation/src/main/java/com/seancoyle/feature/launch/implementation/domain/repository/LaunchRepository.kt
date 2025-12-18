package com.seancoyle.feature.launch.implementation.domain.repository

import androidx.paging.PagingData
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

internal interface LaunchRepository {

    fun pager(): Flow<PagingData<LaunchTypes.Launch>>

    suspend fun upsertAll(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, Throwable>

    suspend fun deleteAll(): LaunchResult<Unit, Throwable>

    suspend fun getTotalEntriesCache(): LaunchResult<Int, Throwable>

    suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes>, Throwable>
}
