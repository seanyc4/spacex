package com.seancoyle.feature.launch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.seancoyle.database.dao.UpcomingLaunchDao
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity
import com.seancoyle.feature.launch.data.remote.UpcomingLaunchesRemoteMediator
import com.seancoyle.feature.launch.di.UpcomingLaunches
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.presentation.LaunchesConstants.INITIAL_LOAD_SIZE
import com.seancoyle.feature.launch.presentation.LaunchesConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.presentation.LaunchesConstants.PREFETCH_DISTANCE
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class UpcomingLaunchesPagerFactory @Inject constructor(
    private val upcomingLaunchDao: UpcomingLaunchDao,
    private val remoteDataSource: LaunchesRemoteDataSource,
    @param:UpcomingLaunches private val upcomingLaunchesLocalDataSource: LaunchesLocalDataSource<UpcomingRemoteKeyEntity>,
    @param:UpcomingLaunches private val upcomingDetailLocalDataSource: DetailLocalDataSource,
) {

    fun create(launchesQuery: LaunchesQuery): Pager<Int, UpcomingLaunchEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGINATION_LIMIT,
                enablePlaceholders = true,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            remoteMediator = UpcomingLaunchesRemoteMediator(
                remotedataSource = remoteDataSource,
                launchesQuery = launchesQuery,
                launchesLocalDataSource = upcomingLaunchesLocalDataSource,
                detailLocalDataSource = upcomingDetailLocalDataSource
            ),
            pagingSourceFactory = { upcomingLaunchDao.pagingSource() }
        )
    }
}
