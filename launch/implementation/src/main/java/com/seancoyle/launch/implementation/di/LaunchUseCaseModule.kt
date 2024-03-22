package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import com.seancoyle.launch.implementation.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.implementation.domain.usecase.CreateMergedLaunchesUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.FilterLaunchItemsInCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetNumLaunchItemsFromCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetNumLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.LaunchesComponentImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

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

    @Binds
    abstract fun bindsLaunchesComponent(
        impl: LaunchesComponentImpl
    ): LaunchesComponent

}