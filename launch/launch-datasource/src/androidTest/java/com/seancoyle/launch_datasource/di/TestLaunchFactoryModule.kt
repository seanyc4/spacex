package com.seancoyle.launch_datasource.di

import com.seancoyle.core_testing.JsonFileReader
import com.seancoyle.launch_datasource.LaunchDataFactory
import com.seancoyle.launch_models.di.LaunchFactoryModule
import com.seancoyle.launch_models.model.launch.LaunchFactory
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
    fun provideLaunchFactory(): LaunchFactory {
        return LaunchFactory()
    }

    @Singleton
    @Provides
    fun provideLaunchDataFactory(
        jsonFileReader: JsonFileReader,
        launchFactory: LaunchFactory
    ): LaunchDataFactory {
        return LaunchDataFactory(
            jsonFileReader = jsonFileReader,
            factory = launchFactory
        )
    }


}