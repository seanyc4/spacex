package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import kotlinx.coroutines.flow.Flow

interface LaunchesComponent {
    fun observeLaunchesUseCase(query: LaunchesQuery): Flow<PagingData<Launch>>
    suspend fun getLaunchPreferencesUseCase(): LaunchPrefs
    suspend fun saveLaunchPreferencesUseCase(order: Order)
}
