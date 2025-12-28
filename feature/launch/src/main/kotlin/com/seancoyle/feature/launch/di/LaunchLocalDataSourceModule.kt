package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.data.local.LaunchLocalDataSourceImpl
import com.seancoyle.feature.launch.data.repository.LaunchLocalDataSource
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
