package com.seancoyle.spacex.di.interactors.launch

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.interactors.launch.*
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