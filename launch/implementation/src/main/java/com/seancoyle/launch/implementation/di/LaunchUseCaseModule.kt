package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import com.seancoyle.launch.implementation.domain.usecase.CreateMergedAndFilteredLaunchesUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.implementation.domain.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.FilterLaunchItemsInCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyFromCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetLaunchesApiAndCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetNumLaunchItemsFromCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetNumLaunchItemsFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.InsertCompanyInfoToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.InsertCompanyInfoToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.InsertLaunchesToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.InsertLaunchesToCacheUseCaseImpl
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
        impl: GetLaunchesApiAndCacheUseCaseImpl
    ): GetLaunchesApiAndCacheUseCase

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
        impl: CreateMergedAndFilteredLaunchesUseCaseImpl
    ): CreateMergedLaunchesUseCase

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
        impl: GetCompanyFromCacheUseCaseImpl
    ): GetCompanyFromCacheUseCase

    @Binds
    abstract fun bindsInsertCompanyInfoToCacheUseCase(
        impl: InsertCompanyInfoToCacheUseCaseImpl
    ): InsertCompanyInfoToCacheUseCase
}