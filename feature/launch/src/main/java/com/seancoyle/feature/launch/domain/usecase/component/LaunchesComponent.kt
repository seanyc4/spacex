package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.feature.launch.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

interface LaunchesComponent {
    fun observeLaunchesUseCase(query: LaunchQuery): Flow<PagingData<LaunchTypes.Launch>>
    suspend fun getLaunchPreferencesUseCase(): LaunchPrefs
    suspend fun saveLaunchPreferencesUseCase(order: Order)
}
