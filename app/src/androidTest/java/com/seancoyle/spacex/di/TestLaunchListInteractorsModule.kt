package com.seancoyle.spacex.di

import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.data.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.business.interactors.launch.*
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [LaunchInteractorsModule::class]
)
object TestLaunchListInteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideLaunchListInteractors(
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