package com.seancoyle.feature.launch.di

import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

private const val LOCAL_URL = "http://localhost:8080/"

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
internal class FakeNetworkModule : NetworkModule() {
    override fun baseUrl() = LOCAL_URL
}
