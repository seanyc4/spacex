package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.data.local.LaunchesPreferencesDataSourceImpl
import com.seancoyle.feature.launch.data.repository.LaunchesPreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchesPreferencesDataSourceModule {

    @Binds
    abstract fun bindLaunchPreferencesDataSource(
        impl: LaunchesPreferencesDataSourceImpl
    ): LaunchesPreferencesDataSource
}
