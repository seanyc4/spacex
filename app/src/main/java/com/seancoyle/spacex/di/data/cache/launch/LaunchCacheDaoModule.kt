package com.seancoyle.spacex.di.data.cache.launch

import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.database.Database
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