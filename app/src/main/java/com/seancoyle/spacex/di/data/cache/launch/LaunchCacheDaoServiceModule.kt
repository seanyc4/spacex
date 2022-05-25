package com.seancoyle.spacex.di.data.cache.launch

import com.seancoyle.spacex.framework.datasource.cache.abstraction.launch.LaunchDaoService
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.implementation.launch.LaunchDaoServiceImpl
import com.seancoyle.spacex.framework.datasource.cache.mappers.launch.LaunchEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchCacheDaoServiceModule {

    @Singleton
    @Provides
    fun provideLaunchDaoService(
        dao: LaunchDao,
        launchEntityMapper: LaunchEntityMapper
    ): LaunchDaoService {
        return LaunchDaoServiceImpl(
            dao = dao,
            entityMapper = launchEntityMapper
        )
    }
}