package com.seancoyle.feature.launch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.entities.LaunchSummaryEntity
import com.seancoyle.feature.launch.data.remote.LaunchRemoteMediator
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.presentation.LaunchesConstants
import javax.inject.Inject

internal class LaunchesPagerFactory @Inject constructor(
    private val launchDao: LaunchDao,
    private val launchesRemoteDataSource: LaunchesRemoteDataSource,
    private val launchesLocalDataSource: LaunchesLocalDataSource
) {
    @OptIn(ExperimentalPagingApi::class)
    fun create(launchesQuery: LaunchesQuery): Pager<Int, LaunchSummaryEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = LaunchesConstants.PAGINATION_LIMIT,
                enablePlaceholders = true,
                initialLoadSize = LaunchesConstants.INITIAL_LOAD_SIZE,
                prefetchDistance = LaunchesConstants.PREFETCH_DISTANCE,
            ),
            remoteMediator = LaunchRemoteMediator(
                launchesRemoteDataSource = launchesRemoteDataSource,
                launchesLocalDataSource = launchesLocalDataSource,
                launchesQuery = launchesQuery
            ),
            pagingSourceFactory = {
                // Filter by launch type to support separate caches per tab
                launchDao.pagingSourceByType(launchesQuery.launchesType.name)
            }
        )
    }
}
