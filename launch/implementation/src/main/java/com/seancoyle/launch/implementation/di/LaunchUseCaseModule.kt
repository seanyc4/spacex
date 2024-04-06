package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetLaunchesApiAndCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetNumLaunchItemsCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetNumLaunchItemsCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.InsertCompanyInfoToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.InsertCompanyInfoToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.InsertLaunchesToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.InsertLaunchesToCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.LaunchesComponentImpl
import com.seancoyle.launch.implementation.domain.usecase.MergedLaunchesCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.MergedLaunchesCacheUseCaseImpl
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
    abstract fun bindsGetLaunchesApiAndCacheUseCase(
        impl: GetLaunchesApiAndCacheUseCaseImpl
    ): GetLaunchesApiAndCacheUseCase

    @Binds
    abstract fun bindsGetNumLaunchItemsCacheUseCase(
        impl: GetNumLaunchItemsCacheUseCaseImpl
    ): GetNumLaunchItemsCacheUseCase

    @Binds
    abstract fun bindsSortAndFilterLaunchesCacheUseCase(
        impl: SortAndFilterLaunchesCacheUseCaseImpl
    ): SortAndFilterLaunchesCacheUseCase

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
        impl: InsertCompanyInfoToCacheUseCaseImpl
    ): InsertCompanyInfoToCacheUseCase
}