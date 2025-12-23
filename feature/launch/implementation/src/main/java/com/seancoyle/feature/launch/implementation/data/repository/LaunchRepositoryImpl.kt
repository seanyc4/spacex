package com.seancoyle.feature.launch.implementation.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.seancoyle.feature.launch.implementation.domain.model.LaunchQuery
import com.seancoyle.feature.launch.implementation.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import com.seancoyle.feature.launch.implementation.data.local.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LaunchRepositoryImpl @Inject constructor(
    private val pagerFactory: LaunchPagerFactory
) : LaunchRepository {

    override fun pager(launchQuery: LaunchQuery): Flow<PagingData<LaunchTypes.Launch>> {
        return pagerFactory.create(launchQuery).flow.map {
            it.map { entity -> entity.toDomain() }
        }
    }

}
