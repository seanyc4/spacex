package com.seancoyle.feature.launch.implementation.di.launch

import com.seancoyle.feature.launch.implementation.data.cache.launch.LaunchLocalDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchDiskDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchDiskDataSourceModule {

    @Binds
    abstract fun bindLaunchDiskDataSource(
        impl: LaunchLocalDataSourceImpl
    ): LaunchDiskDataSource
}