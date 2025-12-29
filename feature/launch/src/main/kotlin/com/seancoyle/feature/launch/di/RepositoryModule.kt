package com.seancoyle.feature.launch.di

import com.seancoyle.feature.launch.data.repository.LaunchPreferencesRepositoryImpl
import com.seancoyle.feature.launch.data.repository.LaunchRepositoryImpl
import com.seancoyle.feature.launch.domain.repository.LaunchPreferencesRepository
import com.seancoyle.feature.launch.domain.repository.LaunchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindLaunchPreferencesRepository(
        impl: LaunchPreferencesRepositoryImpl
    ): LaunchPreferencesRepository

    @Binds
    abstract fun bindLaunchRepository(
        impl: LaunchRepositoryImpl
    ): LaunchRepository

}
