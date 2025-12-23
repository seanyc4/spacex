package com.seancoyle.feature.launch.implementation.domain.repository

import androidx.paging.PagingData
import com.seancoyle.feature.launch.implementation.domain.model.LaunchQuery
import com.seancoyle.feature.launch.implementation.domain.model.LaunchTypes
import kotlinx.coroutines.flow.Flow

internal interface LaunchRepository {
    fun pager(launchQuery: LaunchQuery): Flow<PagingData<LaunchTypes.Launch>>
}
