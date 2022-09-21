package com.seancoyle.spacex.di

import com.seancoyle.spacex.di.data.network.launch.LaunchApiModule
import com.seancoyle.core_testing.JsonFileReader
import com.seancoyle.spacex.datasource.network.launch.FakeLaunchApi
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