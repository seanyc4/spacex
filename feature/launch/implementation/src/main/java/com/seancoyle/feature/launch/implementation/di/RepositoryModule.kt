package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.repository.CompanyRepositoryImpl
import com.seancoyle.feature.launch.implementation.data.repository.LaunchPreferencesRepositoryImpl
import com.seancoyle.feature.launch.implementation.data.repository.LaunchRepositoryImpl
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchPreferencesRepository
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
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

    @Binds
    abstract fun bindCompanyRepository(
        impl: CompanyRepositoryImpl
    ): CompanyRepository
}