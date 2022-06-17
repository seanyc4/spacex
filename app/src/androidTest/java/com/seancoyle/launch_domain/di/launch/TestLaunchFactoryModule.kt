package com.seancoyle.launch_domain.di.launch

import com.seancoyle.launch_domain.di.LaunchFactoryModule
import com.seancoyle.launch_domain.model.launch.LaunchFactory
import com.seancoyle.spacex.framework.datasource.data.LaunchDataFactory
import com.seancoyle.spacex.util.JsonFileReader
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