package com.seancoyle.feature.launch.implementation.di.launch

import com.seancoyle.feature.launch.implementation.data.cache.launch.LaunchLocalDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchLocalDataSourceModule {

    @Binds
    abstract fun bindLaunchLocalDataSource(
        impl: LaunchLocalDataSourceImpl
    ): LaunchLocalDataSource
}