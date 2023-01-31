package com.seancoyle.launch_usecases.di

import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch_datasource.cache.LaunchCacheDataSource
import com.seancoyle.launch_datasource.network.LaunchNetworkDataSource
import com.seancoyle.launch_usecases.launch.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object LaunchUseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideLaunchUseCases(
        @IODispatcher ioDispatcher: CoroutineDispatcher,
        launchCacheDataSource: LaunchCacheDataSource,
        launchNetworkDataSource: LaunchNetworkDataSource
    ): LaunchUseCases {
        return LaunchUseCases(
            getAllLaunchItemsFromCacheUseCase = GetAllLaunchItemsFromCacheUseCase(
                ioDispatcher = ioDispatcher,
                cacheDataSource = launchCacheDataSource
            ),
            getLaunchListFromNetworkAndInsertToCacheUseCase = GetLaunchListFromNetworkAndInsertToCacheUseCase(
                ioDispatcher = ioDispatcher,
                launchNetworkDataSource = launchNetworkDataSource,
                cacheDataSource = launchCacheDataSource
            ),
            getNumLaunchItemsFromCacheUseCase = GetNumLaunchItemsFromCacheUseCase(
                ioDispatcher = ioDispatcher,
                cacheDataSource = launchCacheDataSource
            ),
            filterLaunchItemsInCacheUseCase = FilterLaunchItemsInCacheUseCase(
                ioDispatcher = ioDispatcher,
                cacheDataSource = launchCacheDataSource
            )
        )
    }

}