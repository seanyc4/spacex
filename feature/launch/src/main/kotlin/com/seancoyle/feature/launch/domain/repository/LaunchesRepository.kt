package com.seancoyle.feature.launch.domain.repository

import androidx.paging.PagingData
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.model.LaunchesType
import kotlinx.coroutines.flow.Flow

internal interface LaunchesRepository {
    fun pager(launchesQuery: LaunchesQuery): Flow<PagingData<Launch>>

    suspend fun getLaunch(
        id: String,
        launchType: LaunchesType
    ): LaunchResult<List<Launch>, RemoteError>
}
