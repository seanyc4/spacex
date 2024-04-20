package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.GetCompanyCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetCompanyCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetLaunchPreferencesUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.GetLaunchesApiAndCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.GetNumLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetNumLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.InsertCompanyToCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.InsertCompanyToCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.InsertLaunchesToCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.InsertLaunchesToCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.LaunchesComponent
import com.seancoyle.feature.launch.implementation.domain.usecase.LaunchesComponentImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.PaginateLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.PaginateLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.SaveLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.SaveLaunchPreferencesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class LaunchUseCaseModule {

    @Binds
    abstract fun bindsGetLaunchesApiAndCacheUseCase(
        impl: GetLaunchesApiAndCacheUseCaseImpl
    ): GetLaunchesApiAndCacheUseCase

    @Binds
    abstract fun bindsGetNumLaunchItemsCacheUseCase(
        impl: GetNumLaunchesCacheUseCaseImpl
    ): GetNumLaunchesCacheUseCase

    @Binds
    abstract fun bindsSortAndFilterLaunchesCacheUseCase(
        impl: PaginateLaunchesCacheUseCaseImpl
    ): PaginateLaunchesCacheUseCase

    @Binds
    abstract fun bindsMergedLaunchesCacheUseCase(
        impl: MergedLaunchesCacheUseCaseImpl
    ): MergedLaunchesCacheUseCase

    @Binds
    abstract fun bindsLaunchesComponent(
        impl: LaunchesComponentImpl
    ): LaunchesComponent

    @Binds
    abstract fun bindsInsertLaunchesToCacheUseCase(
        impl: InsertLaunchesToCacheUseCaseImpl
    ): InsertLaunchesToCacheUseCase

    @Binds
    abstract fun bindsGetCompanyApiAndCacheUseCase(
        impl: GetCompanyApiAndCacheUseCaseImpl
    ): GetCompanyApiAndCacheUseCase

    @Binds
    abstract fun bindsGetCompanyCacheUseCase(
        impl: GetCompanyCacheUseCaseImpl
    ): GetCompanyCacheUseCase

    @Binds
    abstract fun bindsInsertCompanyInfoToCacheUseCase(
        impl: InsertCompanyToCacheUseCaseImpl
    ): InsertCompanyToCacheUseCase

    @Binds
    abstract fun bindsSaveLaunchPreferencesUseCase(
        impl: SaveLaunchPreferencesUseCaseImpl
    ): SaveLaunchPreferencesUseCase

    @Binds
    abstract fun bindsGetLaunchPreferencesUseCase(
        impl: GetLaunchPreferencesUseCaseImpl
    ): GetLaunchPreferencesUseCase
}