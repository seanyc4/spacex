package com.seancoyle.feature.launch.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.seancoyle.database.dao.PastLaunchDao
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.feature.launch.data.remote.PastLaunchesRemoteMediator
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.presentation.LaunchesConstants.INITIAL_LOAD_SIZE
import com.seancoyle.feature.launch.presentation.LaunchesConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.presentation.LaunchesConstants.PREFETCH_DISTANCE
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
internal class PastLaunchesPagerFactory @Inject constructor(
    private val pastLaunchDao: PastLaunchDao,
    private val remoteDataSource: LaunchesRemoteDataSource,
    private val localDataSource: PastLaunchesLocalDataSource,
    private val launchDetailLocalDataSource: LaunchDetailLocalDataSource,
    ) {

    fun create(launchesQuery: LaunchesQuery): Pager<Int, PastLaunchEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGINATION_LIMIT,
                enablePlaceholders = true,
                initialLoadSize = INITIAL_LOAD_SIZE,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            remoteMediator = PastLaunchesRemoteMediator(
                launchesRemoteDataSource = remoteDataSource,
                launchesQuery = launchesQuery,
                localDataSource = localDataSource,
                launchDetailLocalDataSource = launchDetailLocalDataSource
            ),
            pagingSourceFactory = { pastLaunchDao.pagingSource() }
        )
    }
}
