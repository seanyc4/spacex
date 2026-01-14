package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.data.local.LaunchDetailLocalDataSourceImpl
import com.seancoyle.feature.launch.data.local.PastLaunchesLocalDataSourceImpl
import com.seancoyle.feature.launch.data.local.UpcomingLaunchesLocalDataSourceImpl
import com.seancoyle.feature.launch.data.repository.LaunchDetailLocalDataSource
import com.seancoyle.feature.launch.data.repository.PastLaunchesLocalDataSource
import com.seancoyle.feature.launch.data.repository.UpcomingLaunchesLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchesLocalDataSourceModule {

    @Binds
    abstract fun bindLaunchDetailLocalDataSource(
        impl: LaunchDetailLocalDataSourceImpl
    ): LaunchDetailLocalDataSource

    @Binds
    abstract fun bindUpcomingLaunchesLocalDataSource(
        impl: UpcomingLaunchesLocalDataSourceImpl
    ): UpcomingLaunchesLocalDataSource

    @Binds
    abstract fun bindPastLaunchesLocalDataSource(
        impl: PastLaunchesLocalDataSourceImpl
    ): PastLaunchesLocalDataSource
}
