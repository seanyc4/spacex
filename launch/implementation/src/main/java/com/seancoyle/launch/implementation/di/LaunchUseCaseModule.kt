package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import com.seancoyle.launch.implementation.domain.usecase.CreateMergedAndFilteredLaunchesCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.CreateMergedLaunchesCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetLaunchesApiAndCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetNumLaunchItemsFromCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetNumLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.InsertCompanyInfoToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.InsertCompanyInfoToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.InsertLaunchesToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.InsertLaunchesToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.LaunchesComponentImpl
import com.seancoyle.launch.implementation.domain.usecase.SortAndFilterLaunchesCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.SortAndFilterLaunchesCacheUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class LaunchUseCaseModule {

    @Binds
    abstract fun bindsGetLaunchListFromNetworkAndInsertToCacheUseCase(
        impl: GetLaunchesApiAndCacheUseCaseImpl
    ): GetLaunchesApiAndCacheUseCase

    @Binds
    abstract fun bindsGetNumLaunchItemsFromCacheUseCase(
        impl: GetNumLaunchItemsFromCacheUseCaseImpl
    ): GetNumLaunchItemsFromCacheUseCase

    @Binds
    abstract fun bindsFilterLaunchesCacheUseCase(
        impl: SortAndFilterLaunchesCacheUseCaseImpl
    ): SortAndFilterLaunchesCacheUseCase

    @Binds
    abstract fun bindsCreateMergedListUseCase(
        impl: CreateMergedAndFilteredLaunchesCacheUseCaseImpl
    ): CreateMergedLaunchesCacheUseCase

    @Binds
    abstract fun bindsLaunchesComponent(
        impl: LaunchesComponentImpl
    ): LaunchesComponent

    @Binds
    abstract fun bindsInsertLaunchesToCacheUseCase(
        impl: InsertLaunchesToCacheUseCaseImpl
    ): InsertLaunchesToCacheUseCase

    @Binds
    abstract fun bindsGetCompanyInfoFromNetworkAndInsertToCacheUseCase(
        impl: GetCompanyApiAndCacheUseCaseImpl
    ): GetCompanyApiAndCacheUseCase

    @Binds
    abstract fun bindsGetCompanyInfoFromCacheUseCase(
        impl: GetCompanyCacheUseCaseImpl
    ): GetCompanyCacheUseCase

    @Binds
    abstract fun bindsInsertCompanyInfoToCacheUseCase(
        impl: InsertCompanyInfoToCacheUseCaseImpl
    ): InsertCompanyInfoToCacheUseCase
}