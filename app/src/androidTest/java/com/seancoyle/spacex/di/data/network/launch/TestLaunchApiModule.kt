package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.launch_datasource.di.network.launch.LaunchApiModule
import com.seancoyle.spacex.framework.datasource.api.launch.FakeLaunchApi
import com.seancoyle.spacex.util.JsonFileReader
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