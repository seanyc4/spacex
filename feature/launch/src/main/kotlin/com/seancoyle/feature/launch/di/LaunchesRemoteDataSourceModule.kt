package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.data.remote.LaunchesRemoteDataSourceImpl
import com.seancoyle.feature.launch.data.repository.LaunchesRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchesRemoteDataSourceModule {

    @Binds
    abstract fun bindLaunchRemoteDataSource(
        impl: LaunchesRemoteDataSourceImpl
    ): LaunchesRemoteDataSource
}
