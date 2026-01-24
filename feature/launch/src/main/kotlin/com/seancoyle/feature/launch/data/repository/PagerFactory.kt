package com.seancoyle.feature.launch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.presentation.LaunchesConstants.INITIAL_LOAD_SIZE
import com.seancoyle.feature.launch.presentation.LaunchesConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.presentation.LaunchesConstants.PREFETCH_DISTANCE

@OptIn(ExperimentalPagingApi::class)
internal class PagerFactory<Entity : Any>(
    private val pagingSourceFactory: () -> PagingSource<Int, Entity>,
    private val remoteMediatorFactory: (LaunchesQuery) -> RemoteMediator<Int, Entity>,
) {

    fun create(launchesQuery: LaunchesQuery): Pager<Int, Entity> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGINATION_LIMIT,
                enablePlaceholders = true,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            remoteMediator = remoteMediatorFactory(launchesQuery),
            pagingSourceFactory = pagingSourceFactory
        )
    }
}
