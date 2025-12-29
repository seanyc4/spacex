package com.seancoyle.core.test

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.MockWebServer
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MockWebServerModule {
    @Provides
    @Singleton
    fun provideMockWebServer(): MockWebServer {
        val server = MockWebServer()
        server.start(8080)
        return server
    }
}
