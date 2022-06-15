package com.seancoyle.launch_datasource.di.cache.launch

import com.seancoyle.data.daos.LaunchDao
import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource.cache.implementation.launch.LaunchCacheDataSourceImpl
import com.seancoyle.launch_datasource.cache.mappers.launch.LaunchEntityMapper
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