package com.seancoyle.spacex.di

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
            getLaunchItemByIdFromCache = GetLaunchItemByIdFromCache(
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
            insertLaunchListToCache = InsertLaunchListToCache(
                cacheDataSource = launchCacheDataSource,
                factory = launchFactory
            ),
            filterLaunchItemsInCache = FilterLaunchItemsInCache(
                cacheDataSource = launchCacheDataSource
            )

        )
    }

}