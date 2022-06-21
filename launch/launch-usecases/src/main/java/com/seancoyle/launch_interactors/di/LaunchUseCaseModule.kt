package com.seancoyle.launch_interactors.di

import com.seancoyle.launch_datasource.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.launch_datasource.network.abstraction.launch.LaunchNetworkDataSource
import com.seancoyle.launch_interactors.launch.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LaunchUseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideLaunchUseCases(
        launchCacheDataSource: LaunchCacheDataSource,
        launchNetworkDataSource: LaunchNetworkDataSource
    ): LaunchUseCase {
        return LaunchUseCase(
            getAllLaunchItemsFromCacheUseCase = GetAllLaunchItemsFromCacheUseCase(
                cacheDataSource = launchCacheDataSource
            ),
            getLaunchListFromNetworkAndInsertToCacheUseCase = GetLaunchListFromNetworkAndInsertToCacheUseCase(
                launchNetworkDataSource = launchNetworkDataSource,
                cacheDataSource = launchCacheDataSource
            ),
            getNumLaunchItemsFromCacheUseCase = GetNumLaunchItemsFromCacheUseCase(
                cacheDataSource = launchCacheDataSource
            ),
            filterLaunchItemsInCacheUseCase = FilterLaunchItemsInCacheUseCase(
                cacheDataSource = launchCacheDataSource
            )
        )
    }

}