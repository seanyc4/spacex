package com.seancoyle.feature.launch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.seancoyle.feature.launch.domain.model.LaunchQuery
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.feature.launch.LaunchConstants
import com.seancoyle.feature.launch.data.remote.LaunchRemoteMediator
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
                pageSize = LaunchConstants.PAGINATION_LIMIT,
                enablePlaceholders = false,
                initialLoadSize = LaunchConstants.INITIAL_LOAD_SIZE,
                prefetchDistance = LaunchConstants.PREFETCH_DISTANCE,
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
