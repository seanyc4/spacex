package com.seancoyle.spacex.di.data.launch

import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.implementation.di.LaunchApiModule
import com.seancoyle.spacex.data.network.launch.FakeLaunchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LaunchApiModule::class]
)
object TestLaunchApiModule {

    @Singleton
    @Provides
    fun provideLaunchApiService(
        jsonFileReader: JsonFileReader
    ): FakeLaunchApi {
        return FakeLaunchApi(
            jsonFileReader = jsonFileReader
        )
    }

}