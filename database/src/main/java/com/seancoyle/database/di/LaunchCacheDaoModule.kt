package com.seancoyle.database.di

import com.seancoyle.database.Database
import com.seancoyle.database.dao.LaunchDetailDao
import com.seancoyle.database.dao.PastLaunchDao
import com.seancoyle.database.dao.PastRemoteKeyDao
import com.seancoyle.database.dao.UpcomingLaunchDao
import com.seancoyle.database.dao.UpcomingRemoteKeyDao
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
    fun provideLaunchDetailDao(database: Database): LaunchDetailDao {
        return database.launchDetailDao()
    }

    @Singleton
    @Provides
    fun provideUpcomingLaunchDao(database: Database): UpcomingLaunchDao {
        return database.upcomingLaunchDao()
    }

    @Singleton
    @Provides
    fun provideUpcomingRemoteKeyDao(database: Database): UpcomingRemoteKeyDao {
        return database.upcomingRemoteKeyDao()
    }

    @Singleton
    @Provides
    fun providePastLaunchDao(database: Database): PastLaunchDao {
        return database.pastLaunchDao()
    }

    @Singleton
    @Provides
    fun providePastRemoteKeyDao(database: Database): PastRemoteKeyDao {
        return database.pastRemoteKeyDao()
    }
}
