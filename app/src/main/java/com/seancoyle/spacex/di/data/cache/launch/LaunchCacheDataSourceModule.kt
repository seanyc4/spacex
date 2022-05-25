package com.seancoyle.spacex.di.data.cache.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.cache.implementation.launch.LaunchCacheDataSourceImpl
import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
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
        daoService: LaunchDaoService
    ): LaunchCacheDataSource {
        return LaunchCacheDataSourceImpl(
            daoService = daoService
        )
    }
}