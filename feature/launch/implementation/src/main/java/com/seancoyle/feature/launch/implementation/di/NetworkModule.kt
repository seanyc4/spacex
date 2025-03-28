package com.seancoyle.feature.launch.implementation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.seancoyle.feature.launch.implementation.BuildConfig

const val BASE_URL = "https://api.spacexdata.com/"

@Module
@InstallIn(SingletonComponent::class)
open class NetworkModule {
    protected open fun baseUrl() = BASE_URL

    @Singleton
    @Provides
    internal fun providesProdRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

        return Retrofit.Builder()
            .baseUrl(baseUrl())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    internal fun providesOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
            .callTimeout(10L, TimeUnit.SECONDS)
            .connectTimeout(10L, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            client.addInterceptor(interceptor)
        }

        return client.build()
    }

}
