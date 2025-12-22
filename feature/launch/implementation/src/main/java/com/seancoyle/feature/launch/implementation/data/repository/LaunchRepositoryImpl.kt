package com.seancoyle.feature.launch.implementation.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.domain.model.LaunchQuery
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.implementation.domain.model.LaunchStatus
import com.seancoyle.feature.launch.implementation.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import com.seancoyle.feature.launch.implementation.data.local.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LaunchRepositoryImpl @Inject constructor(
    private val launchLocalDataSource: LaunchLocalDataSource,
    private val pagerFactory: LaunchPagerFactory
) : LaunchRepository {

    override fun pager(launchQuery: LaunchQuery): Flow<PagingData<LaunchTypes.Launch>> {
        return pagerFactory.create(launchQuery).flow.map {
            it.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun upsertAll(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, Throwable> {
        return launchLocalDataSource.upsertAll(launches)
    }

    override suspend fun paginateCache(
        launchYear: String,
        order: Order,
        launchStatus: LaunchStatus,
        page: Int
    ): LaunchResult<List<LaunchTypes>, Throwable> {
        return launchLocalDataSource.paginate(
            launchYear = launchYear,
            order = order,
            launchStatus = launchStatus,
            page = page
        )
    }


    override suspend fun deleteAll(): LaunchResult<Unit, Throwable> {
        return launchLocalDataSource.deleteAll()
    }

    override suspend fun getTotalEntriesCache(): LaunchResult<Int, Throwable> {
        return launchLocalDataSource.getTotalEntries()
    }
}
