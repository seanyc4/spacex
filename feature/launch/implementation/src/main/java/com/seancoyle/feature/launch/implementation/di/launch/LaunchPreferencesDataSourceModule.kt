package com.seancoyle.feature.launch.implementation.di.launch

import com.seancoyle.feature.launch.implementation.data.repository.launch.LaunchPreferencesDataSource
import com.seancoyle.feature.launch.implementation.data.local.launch.LaunchPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchPreferencesDataSourceModule {

    @Binds
    abstract fun bindLaunchPreferencesDataSource(
        impl: LaunchPreferencesDataSourceImpl
    ): LaunchPreferencesDataSource
}