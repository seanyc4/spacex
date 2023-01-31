package com.seancoyle.launch_datasource.di.cache

import com.seancoyle.database.daos.LaunchDao
import com.seancoyle.launch_datasource.cache.LaunchCacheDataSource
import com.seancoyle.launch_datasource.cache.LaunchCacheDataSourceImpl
import com.seancoyle.launch_datasource.cache.LaunchEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchCacheDataSourceModule {

    @Singleton
    @Provides
    fun provideLaunchCacheDataSource(
        dao: LaunchDao,
        launchEntityMapper: LaunchEntityMapper
    ): LaunchCacheDataSource {
        return LaunchCacheDataSourceImpl(
            dao = dao,
            entityMapper = launchEntityMapper
        )
    }
}