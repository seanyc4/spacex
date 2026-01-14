package com.seancoyle.database.di

import com.seancoyle.database.Database
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.LaunchDetailDao
import com.seancoyle.database.dao.LaunchRemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LaunchCacheDaoModule {

    @Singleton
    @Provides
    fun provideLaunchDao(database: Database): LaunchDao {
        return database.launchDao()
    }

    @Singleton
    @Provides
    fun provideLaunchDetailDao(database: Database): LaunchDetailDao {
        return database.launchDetailDao()
    }

    @Singleton
    @Provides
    fun provideLaunchRemoteKeyDao(database: Database): LaunchRemoteKeyDao {
        return database.launchRemoteKeyDao()
    }
}
