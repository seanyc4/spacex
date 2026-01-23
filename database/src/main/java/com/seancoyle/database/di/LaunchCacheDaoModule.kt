package com.seancoyle.database.di

import com.seancoyle.database.Database
import com.seancoyle.database.dao.PastDetailDao
import com.seancoyle.database.dao.PastLaunchDao
import com.seancoyle.database.dao.PastRemoteKeyDao
import com.seancoyle.database.dao.UpcomingDetailDao
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
    fun provideUpcomingDetailDao(database: Database): UpcomingDetailDao {
        return database.upcomingDetailDao()
    }

    @Singleton
    @Provides
    fun providePastDetailDao(database: Database): PastDetailDao {
        return database.pastDetailDao()
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
