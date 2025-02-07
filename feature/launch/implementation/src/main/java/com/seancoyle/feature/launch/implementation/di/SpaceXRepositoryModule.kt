package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.repository.SpaceXRepositoryImpl
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SpaceXRepositoryModule {

    @Binds
    abstract fun bindSpaceXRepository(
        impl: SpaceXRepositoryImpl
    ): SpaceXRepository
}