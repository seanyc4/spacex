package com.seancoyle.core.datastore.di

import com.seancoyle.core.datastore.data.LaunchPreferencesDataSourceImpl
import com.seancoyle.feature.launch.api.domain.cache.LaunchPreferencesDataSource
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