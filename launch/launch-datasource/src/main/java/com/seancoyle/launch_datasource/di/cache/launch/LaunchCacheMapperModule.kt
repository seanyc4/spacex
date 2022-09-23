package com.seancoyle.launch_datasource.di.cache.launch

import com.seancoyle.launch_datasource.cache.mappers.launch.LaunchEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchCacheMapperModule {

    @Singleton
    @Provides
    fun provideLaunchCacheMapper(): LaunchEntityMapper {
        return LaunchEntityMapper()
    }
}