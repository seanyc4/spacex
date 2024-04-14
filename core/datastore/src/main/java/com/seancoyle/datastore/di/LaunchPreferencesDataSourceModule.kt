package com.seancoyle.datastore.di

import com.seancoyle.datastore.data.LaunchPreferencesDataSourceImpl
import com.seancoyle.launch.api.domain.cache.LaunchPreferencesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LaunchPreferencesDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindsLaunchPreferencesDataSource(
        impl: LaunchPreferencesDataSourceImpl
    ): LaunchPreferencesDataSource
}