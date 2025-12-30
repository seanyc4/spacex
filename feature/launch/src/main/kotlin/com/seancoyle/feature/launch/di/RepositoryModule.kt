package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.data.repository.LaunchesPreferencesRepositoryImpl
import com.seancoyle.feature.launch.data.repository.LaunchesRepositoryImpl
import com.seancoyle.feature.launch.domain.repository.LaunchesPreferencesRepository
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindLaunchPreferencesRepository(
        impl: LaunchesPreferencesRepositoryImpl
    ): LaunchesPreferencesRepository

    @Binds
    abstract fun bindLaunchRepository(
        impl: LaunchesRepositoryImpl
    ): LaunchesRepository

}
