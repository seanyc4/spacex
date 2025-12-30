package com.seancoyle.feature.launch.domain.repository

import androidx.paging.PagingData
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import kotlinx.coroutines.flow.Flow

internal interface LaunchesRepository {
    fun pager(launchesQuery: LaunchesQuery): Flow<PagingData<Launch>>
}
