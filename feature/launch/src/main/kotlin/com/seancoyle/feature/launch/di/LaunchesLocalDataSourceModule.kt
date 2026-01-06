package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.data.local.LaunchesLocalDataSourceImpl
import com.seancoyle.feature.launch.data.repository.LaunchesLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchesLocalDataSourceModule {

    @Binds
    abstract fun bindLaunchLocalDataSource(
        impl: LaunchesLocalDataSourceImpl
    ): LaunchesLocalDataSource
}
