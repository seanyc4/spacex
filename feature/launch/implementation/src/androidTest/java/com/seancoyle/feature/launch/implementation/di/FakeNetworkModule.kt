package com.seancoyle.feature.launch.implementation.di

import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
internal class FakeNetworkModule : NetworkModule() {
    override fun baseUrl() = "http://localhost:8080/"
}