package com.seancoyle.feature.launch.di

import com.seancoyle.core.data.NetworkConstants.NETWORK_TIMEOUT_SECONDS
import com.seancoyle.feature.launch.BuildConfig
import com.seancoyle.feature.launch.data.remote.RetryInterceptor
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

const val PROD_BASE_URL = "https://ll.thespacedevs.com/"
const val DEV_BASE_URL = "https://lldev.thespacedevs.com/"

@Module
@InstallIn(SingletonComponent::class)
open class NetworkModule {
    protected open fun baseUrl() = if (BuildConfig.DEBUG) DEV_BASE_URL else PROD_BASE_URL

    @Singleton
    @Provides
    internal fun providesProdRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            coerceInputValues = true
            explicitNulls = false
            encodeDefaults = true
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
            .callTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(RetryInterceptor())

        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            client.addInterceptor(interceptor)
        }

        return client.build()
    }
}
