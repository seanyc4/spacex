package com.seancoyle.feature.launch.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.seancoyle.database.dao.PastLaunchDao
import com.seancoyle.database.dao.UpcomingLaunchDao
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.PastRemoteKeyEntity
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity
import com.seancoyle.feature.launch.data.remote.PastLaunchesRemoteMediator
import com.seancoyle.feature.launch.data.remote.UpcomingLaunchesRemoteMediator
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import com.seancoyle.feature.launch.data.repository.PagerFactory
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object LaunchesPagingModule {

    @Provides
    @UpcomingLaunches
    @OptIn(ExperimentalPagingApi::class)
    fun provideUpcomingPagerFactory(
        upcomingLaunchDao: UpcomingLaunchDao,
        remoteDataSource: LaunchesRemoteDataSource,
        @UpcomingLaunches launchesLocalDataSource: LaunchesLocalDataSource<UpcomingRemoteKeyEntity>,
        @UpcomingLaunches detailLocalDataSource: DetailLocalDataSource,
    ): PagerFactory<UpcomingLaunchEntity> {
        val mediatorFactory: (LaunchesQuery) -> RemoteMediator<Int, UpcomingLaunchEntity> = { query ->
            UpcomingLaunchesRemoteMediator(
                remoteDataSource = remoteDataSource,
                launchesLocalDataSource = launchesLocalDataSource,
                detailLocalDataSource = detailLocalDataSource,
                launchesQuery = query
            )
        }

        return PagerFactory(
            pagingSourceFactory = { upcomingLaunchDao.pagingSource() },
            remoteMediatorFactory = mediatorFactory
        )
    }

    @Provides
    @PastLaunches
    @OptIn(ExperimentalPagingApi::class)
    fun providePastPagerFactory(
        pastLaunchDao: PastLaunchDao,
        remoteDataSource: LaunchesRemoteDataSource,
        @PastLaunches launchesLocalDataSource: LaunchesLocalDataSource<PastRemoteKeyEntity>,
        @PastLaunches detailLocalDataSource: DetailLocalDataSource,
    ): PagerFactory<PastLaunchEntity> {
        val mediatorFactory: (LaunchesQuery) -> RemoteMediator<Int, PastLaunchEntity> = { query ->
            PastLaunchesRemoteMediator(
                remoteDataSource = remoteDataSource,
                launchesLocalDataSource = launchesLocalDataSource,
                detailLocalDataSource = detailLocalDataSource,
                launchesQuery = query
            )
        }

        return PagerFactory(
            pagingSourceFactory = { pastLaunchDao.pagingSource() },
            remoteMediatorFactory = mediatorFactory
        )
    }
}
