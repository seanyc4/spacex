package com.seancoyle.launch_datasource.di.cache.launch

import com.seancoyle.launch_datasource.cache.Database
import com.seancoyle.launch_datasource.cache.dao.launch.LaunchDao
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