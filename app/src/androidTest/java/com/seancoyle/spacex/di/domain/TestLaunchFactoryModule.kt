package com.seancoyle.spacex.di.domain

import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.api.LaunchFactory
import com.seancoyle.launch_models.model.di.LaunchFactoryModule
import com.seancoyle.spacex.LaunchDataFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LaunchFactoryModule::class]
)
object TestLaunchFactoryModule {


    @Singleton
    @Provides
    fun provideLaunchFactory(): com.seancoyle.launch.api.LaunchFactory {
        return com.seancoyle.launch.api.LaunchFactory()
    }

    @Singleton
    @Provides
    fun provideLaunchDataFactory(
        jsonFileReader: JsonFileReader,
        launchFactory: com.seancoyle.launch.api.LaunchFactory
    ): LaunchDataFactory {
        return LaunchDataFactory(
            jsonFileReader = jsonFileReader,
            factory = launchFactory
        )
    }


}