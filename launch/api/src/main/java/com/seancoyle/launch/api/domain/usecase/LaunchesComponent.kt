package com.seancoyle.launch.api.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.launch.api.domain.model.Launch
import com.seancoyle.launch.api.domain.model.ViewType
import kotlinx.coroutines.flow.Flow

interface LaunchesComponent {

    suspend fun filterLaunchItemsInCacheUseCase(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<ViewType>?>

    suspend fun createMergeLaunchesUseCase(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<List<ViewType>>

    suspend fun getLaunchesFromNetworkAndInsertToCacheUseCase(): Flow<ApiResult<List<Launch>>>

}