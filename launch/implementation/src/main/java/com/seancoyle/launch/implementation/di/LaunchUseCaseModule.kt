package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.contract.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.contract.domain.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.contract.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.contract.domain.usecase.GetNumLaunchItemsFromCacheUseCase
import com.seancoyle.launch.implementation.domain.CreateMergedLaunchesUseCaseImpl
import com.seancoyle.launch.implementation.domain.FilterLaunchItemsInCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetNumLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.LaunchUseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class LaunchUseCaseModule {

    @Binds
    abstract fun bindsGetLaunchListFromNetworkAndInsertToCacheUseCase(
        impl: GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl
    ): GetLaunchesFromNetworkAndInsertToCacheUseCase


    @Binds
    abstract fun bindsGetNumLaunchItemsFromCacheUseCase(
        impl: GetNumLaunchItemsFromCacheUseCaseImpl
    ): GetNumLaunchItemsFromCacheUseCase

    @Binds
    abstract fun bindsFilterLaunchItemsInCacheUseCase(
        impl: FilterLaunchItemsInCacheUseCaseImpl
    ): FilterLaunchItemsInCacheUseCase

    @Binds
    abstract fun bindsCreateMergedListUseCase(
        impl: CreateMergedLaunchesUseCaseImpl
    ): CreateMergedLaunchesUseCase

    companion object {
        @ViewModelScoped
        @Provides
        fun provideLaunchUseCases(
            getLaunchesFromNetworkAndInsertToCacheUseCase: GetLaunchesFromNetworkAndInsertToCacheUseCase,
            filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
        ): LaunchUseCases {
            return LaunchUseCases(
                getLaunchesFromNetworkAndInsertToCacheUseCase = getLaunchesFromNetworkAndInsertToCacheUseCase,
                filterLaunchItemsInCacheUseCase = filterLaunchItemsInCacheUseCase
            )
        }
    }
}