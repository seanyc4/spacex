package com.seancoyle.core.test

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.mockwebserver.MockWebServer

@Module
@InstallIn(SingletonComponent::class)
internal object MockWebServerModule {
    @Provides
    fun provideMockWebServer(): MockWebServer {
        val server = MockWebServer()
        server.start(8080)
        return server
    }
}
