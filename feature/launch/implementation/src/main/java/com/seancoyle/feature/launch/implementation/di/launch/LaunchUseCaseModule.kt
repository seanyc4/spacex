package com.seancoyle.feature.launch.implementation.di.launch

import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyApiAndCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchPreferencesUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchesApiAndCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetNumLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetNumLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.GetSpaceXDataUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.GetSpaceXDataUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponentImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.PaginateLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.PaginateLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.SaveLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.SaveLaunchPreferencesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class LaunchUseCaseModule {

    @Binds
    abstract fun bindGetSpaceXDataUseCase(
        impl: GetSpaceXDataUseCaseImpl
    ): GetSpaceXDataUseCase

    @Binds
    abstract fun bindGetLaunchesApiAndCacheUseCase(
        impl: GetLaunchesApiAndCacheUseCaseImpl
    ): GetLaunchesApiAndCacheUseCase

    @Binds
    abstract fun bindGetNumLaunchItemsCacheUseCase(
        impl: GetNumLaunchesCacheUseCaseImpl
    ): GetNumLaunchesCacheUseCase

    @Binds
    abstract fun bindPaginateLaunchesCacheUseCase(
        impl: PaginateLaunchesCacheUseCaseImpl
    ): PaginateLaunchesCacheUseCase

    @Binds
    abstract fun bindMergedLaunchesCacheUseCase(
        impl: MergedLaunchesCacheUseCaseImpl
    ): MergedLaunchesCacheUseCase

    @Binds
    abstract fun bindLaunchesComponent(
        impl: LaunchesComponentImpl
    ): LaunchesComponent

    @Binds
    abstract fun bindGetCompanyApiAndCacheUseCase(
        impl: GetCompanyApiAndCacheUseCaseImpl
    ): GetCompanyApiAndCacheUseCase

    @Binds
    abstract fun bindGetCompanyCacheUseCase(
        impl: GetCompanyCacheUseCaseImpl
    ): GetCompanyCacheUseCase

    @Binds
    abstract fun bindSaveLaunchPreferencesUseCase(
        impl: SaveLaunchPreferencesUseCaseImpl
    ): SaveLaunchPreferencesUseCase

    @Binds
    abstract fun bindGetLaunchPreferencesUseCase(
        impl: GetLaunchPreferencesUseCaseImpl
    ): GetLaunchPreferencesUseCase
}