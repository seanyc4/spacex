package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import kotlinx.coroutines.flow.Flow

interface LaunchesComponent {

    fun observeUpcomingLaunches(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>>

    fun observePastLaunches(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>>

    suspend fun getLaunchPreferencesUseCase(): LaunchPrefs

    suspend fun saveLaunchPreferencesUseCase(order: Order)

    suspend fun getLaunchUseCase(
        launchId: String,
        launchType: LaunchesType,
        isRefresh: Boolean = false
    ): LaunchResult<Launch, RemoteError>
}
