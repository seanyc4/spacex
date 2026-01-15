package com.seancoyle.feature.launch.di

import com.seancoyle.database.entities.PastRemoteKeyEntity
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity
import com.seancoyle.feature.launch.data.local.PastDetailLocalDataSourceImpl
import com.seancoyle.feature.launch.data.local.PastLaunchesLocalDataSourceImpl
import com.seancoyle.feature.launch.data.local.UpcomingDetailLocalDataSourceImpl
import com.seancoyle.feature.launch.data.local.UpcomingLaunchesLocalDataSourceImpl
import com.seancoyle.feature.launch.data.repository.DetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchesLocalDataSourceModule {

    @Binds
    @UpcomingLaunches
    abstract fun bindUpcomingDetailLocalDataSource(
        impl: UpcomingDetailLocalDataSourceImpl
    ): DetailLocalDataSource

    @Binds
    @PastLaunches
    abstract fun bindPastDetailLocalDataSource(
        impl: PastDetailLocalDataSourceImpl
    ): DetailLocalDataSource

    @Binds
    @UpcomingLaunches
    abstract fun bindUpcomingLaunchesLocalDataSource(
        impl: UpcomingLaunchesLocalDataSourceImpl
    ): LaunchesLocalDataSource<UpcomingRemoteKeyEntity>

    @Binds
    @PastLaunches
    abstract fun bindPastLaunchesLocalDataSource(
        impl: PastLaunchesLocalDataSourceImpl
    ): LaunchesLocalDataSource<PastRemoteKeyEntity>
}
