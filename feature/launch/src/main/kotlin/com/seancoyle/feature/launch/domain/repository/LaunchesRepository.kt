package com.seancoyle.feature.launch.domain.repository

import androidx.paging.PagingData
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import kotlinx.coroutines.flow.Flow

internal interface LaunchesRepository {

    fun upcomingPager(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>>

    fun pastPager(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>>

    suspend fun getLaunch(
        id: String,
        launchType: LaunchesType,
        isRefresh: Boolean
    ): LaunchResult<Launch, RemoteError>
}
