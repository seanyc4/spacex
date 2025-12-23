package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchPreferencesUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.launch.GetLaunchPreferencesUseCaseImpl
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.implementation.domain.usecase.component.LaunchesComponentImpl
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
