package com.seancoyle.launch_interactors.di

import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.launch_domain.model.launch.LaunchFactory
import com.seancoyle.launch_interactors.launch.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LaunchInteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideLaunchInteractors(
        launchCacheDataSource: LaunchCacheDataSource,
        launchNetworkDataSource: LaunchNetworkDataSource,
        launchFactory: LaunchFactory
    ): LaunchInteractors {
        return LaunchInteractors(
            getAllLaunchItemsFromCache = GetAllLaunchItemsFromCache(
                cacheDataSource = launchCacheDataSource
            ),
            getLaunchListFromNetworkAndInsertToCache = GetLaunchListFromNetworkAndInsertToCache(
                cacheDataSource = launchCacheDataSource,
                launchNetworkDataSource = launchNetworkDataSource,
                factory = launchFactory
            ),
            getNumLaunchItemsFromCache = GetNumLaunchItemsFromCache(
                cacheDataSource = launchCacheDataSource
            ),
            filterLaunchItemsInCache = FilterLaunchItemsInCache(
                cacheDataSource = launchCacheDataSource
            )

        )
    }

}