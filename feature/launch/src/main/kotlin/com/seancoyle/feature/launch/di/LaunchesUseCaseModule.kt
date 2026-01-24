package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.domain.usecase.analytics.LaunchAnalyticsComponent
import com.seancoyle.feature.launch.domain.usecase.analytics.LaunchAnalyticsComponentImpl
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponent
import com.seancoyle.feature.launch.domain.usecase.component.LaunchesComponentImpl
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchesPreferencesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchesPreferencesUseCaseImpl
import com.seancoyle.feature.launch.domain.usecase.launch.SaveLaunchesPreferencesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.SaveLaunchesPreferencesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class LaunchesUseCaseModule {

    @Binds
    abstract fun bindLaunchesComponent(
        impl: LaunchesComponentImpl
    ): LaunchesComponent

    @Binds
    abstract fun bindLaunchAnalyticsComponent(
        impl: LaunchAnalyticsComponentImpl
    ): LaunchAnalyticsComponent

    @Binds
    abstract fun bindSaveLaunchPreferencesUseCase(
        impl: SaveLaunchesPreferencesUseCaseImpl
    ): SaveLaunchesPreferencesUseCase

    @Binds
    abstract fun bindGetLaunchPreferencesUseCase(
        impl: GetLaunchesPreferencesUseCaseImpl
    ): GetLaunchesPreferencesUseCase
}
