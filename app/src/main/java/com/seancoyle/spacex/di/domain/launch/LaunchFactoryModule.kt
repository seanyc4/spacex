package com.seancoyle.spacex.di.domain.launch

import com.seancoyle.launch_domain.model.launch.LaunchFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchFactoryModule {

    @Singleton
    @Provides
    fun provideLaunchFactory(): LaunchFactory {
        return LaunchFactory()
    }
}