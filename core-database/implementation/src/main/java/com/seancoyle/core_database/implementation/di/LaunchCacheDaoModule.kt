package com.seancoyle.core_database.implementation.di

import com.seancoyle.core_database.api.LaunchDao
import com.seancoyle.core_database.implementation.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchCacheDaoModule {

    @Singleton
    @Provides
    fun provideLaunchDao(database: Database): LaunchDao {
        return database.launchDao()
    }
}