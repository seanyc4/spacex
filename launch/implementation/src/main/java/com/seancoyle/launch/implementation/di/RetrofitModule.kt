package com.seancoyle.launch.implementation.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

const val BASE_URL = "https://api.spacexdata.com/"

@Module
@InstallIn(SingletonComponent::class)
internal object RetrofitModule {
    @Singleton
    @Provides
    fun provideLaunchRetrofitBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(OkHttpClient())
            .build()
    }
}