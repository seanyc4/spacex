package com.seancoyle.spacex.di.data.launch

import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.spacex.framework.datasource.network.launch.FakeLaunchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [com.seancoyle.launch.implementation.di.LaunchApiModule::class]
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