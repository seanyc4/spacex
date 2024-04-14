package com.seancoyle.core.datastore.di

import com.seancoyle.core.data.AppPreferencesDataSource
import com.seancoyle.core.datastore.data.AppPreferencesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppPreferencesDataSourceModule {
    @Singleton
    @Binds
    abstract fun bindsAppPreferencesDataSource(
        impl: AppPreferencesDataSourceImpl
    ): AppPreferencesDataSource
}