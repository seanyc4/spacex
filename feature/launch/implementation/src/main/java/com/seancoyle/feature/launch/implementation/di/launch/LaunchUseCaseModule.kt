package com.seancoyle.feature.launch.implementation.di.launch

import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchPreferencesUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchesApiAndCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetNumLaunchesCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetNumLaunchesCacheUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponentImpl
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
    abstract fun bindLaunchesComponent(
        impl: LaunchesComponentImpl
    ): LaunchesComponent

    @Binds
    abstract fun bindSaveLaunchPreferencesUseCase(
        impl: SaveLaunchPreferencesUseCaseImpl
    ): SaveLaunchPreferencesUseCase

    @Binds
    abstract fun bindGetLaunchPreferencesUseCase(
        impl: GetLaunchPreferencesUseCaseImpl
    ): GetLaunchPreferencesUseCase
}
