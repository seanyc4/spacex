package com.seancoyle.feature.launch.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.seancoyle.feature.launch.data.local.toDomain
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LaunchesRepositoryImpl @Inject constructor(
    private val pagerFactory: LaunchesPagerFactory
) : LaunchesRepository {

    override fun pager(launchesQuery: LaunchesQuery): Flow<PagingData<Launch>> {
        return pagerFactory.create(launchesQuery).flow.map {
            it.map { entity -> entity.toDomain() }
        }
    }

}
