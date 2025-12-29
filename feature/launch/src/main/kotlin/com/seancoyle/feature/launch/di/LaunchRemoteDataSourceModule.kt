package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.data.remote.LaunchRemoteDataSourceImpl
import com.seancoyle.feature.launch.data.repository.LaunchRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchRemoteDataSourceModule {

    @Binds
    abstract fun bindLaunchRemoteDataSource(
        impl: LaunchRemoteDataSourceImpl
    ): LaunchRemoteDataSource
}
