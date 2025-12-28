package com.seancoyle.feature.launch.domain.repository

import androidx.paging.PagingData
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import kotlinx.coroutines.flow.Flow

internal interface LaunchRepository {
    fun pager(launchQuery: LaunchQuery): Flow<PagingData<Launch>>
}
