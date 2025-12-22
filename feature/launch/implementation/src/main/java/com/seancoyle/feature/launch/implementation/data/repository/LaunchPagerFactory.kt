package com.seancoyle.feature.launch.implementation.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.seancoyle.feature.launch.implementation.domain.model.LaunchQuery
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.feature.launch.api.LaunchConstants.INITIAL_LOAD_SIZE
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.api.LaunchConstants.PREFETCH_DISTANCE
import com.seancoyle.feature.launch.implementation.data.remote.LaunchRemoteMediator
import javax.inject.Inject

internal class LaunchPagerFactory @Inject constructor(
    private val launchDao: LaunchDao,
    private val launchRemoteDataSource: LaunchRemoteDataSource,
    private val launchLocalDataSource: LaunchLocalDataSource
) {
    @OptIn(ExperimentalPagingApi::class)
    fun create(launchQuery: LaunchQuery): Pager<Int, LaunchEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGINATION_LIMIT,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
            ),
            remoteMediator = LaunchRemoteMediator(
                launchRemoteDataSource = launchRemoteDataSource,
                launchLocalDataSource = launchLocalDataSource,
                launchQuery = launchQuery
            ),
            pagingSourceFactory = {
                launchDao.pagingSource()
            }
        )
    }
}
