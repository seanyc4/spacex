package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.usecase.CreateMergedListUseCase
import com.seancoyle.launch.api.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.api.usecase.GetAllLaunchItemsFromCacheUseCase
import com.seancoyle.launch.api.usecase.GetLaunchListFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.api.usecase.GetNumLaunchItemsFromCacheUseCase
import com.seancoyle.launch.implementation.domain.CreateMergedListUseCaseImpl
import com.seancoyle.launch.implementation.domain.FilterLaunchItemsInCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetAllLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetLaunchListFromNetworkAndInsertToCacheUseCaseImpl
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
    abstract fun bindsGetAllLaunchItemsFromCacheUseCase(
        impl: GetAllLaunchItemsFromCacheUseCaseImpl
    ): GetAllLaunchItemsFromCacheUseCase

    @Binds
    abstract fun bindsGetLaunchListFromNetworkAndInsertToCacheUseCase(
        impl: GetLaunchListFromNetworkAndInsertToCacheUseCaseImpl
    ): GetLaunchListFromNetworkAndInsertToCacheUseCase


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
        impl: CreateMergedListUseCaseImpl
    ): CreateMergedListUseCase

    companion object {
        @ViewModelScoped
        @Provides
        fun provideLaunchUseCases(
            getAllLaunchItemsFromCacheUseCase: GetAllLaunchItemsFromCacheUseCase,
            getLaunchListFromNetworkAndInsertToCacheUseCase: GetLaunchListFromNetworkAndInsertToCacheUseCase,
            getNumLaunchItemsFromCacheUseCase: GetNumLaunchItemsFromCacheUseCase,
            filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
        ): LaunchUseCases {
            return LaunchUseCases(
                getAllLaunchItemsFromCacheUseCase = getAllLaunchItemsFromCacheUseCase,
                getLaunchListFromNetworkAndInsertToCacheUseCase = getLaunchListFromNetworkAndInsertToCacheUseCase,
                getNumLaunchItemsFromCacheUseCase = getNumLaunchItemsFromCacheUseCase,
                filterLaunchItemsInCacheUseCase = filterLaunchItemsInCacheUseCase
            )
        }
    }
}