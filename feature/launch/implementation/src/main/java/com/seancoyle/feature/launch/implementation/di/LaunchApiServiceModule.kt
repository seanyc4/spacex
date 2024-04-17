package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.network.LaunchApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object LaunchApiServiceModule {

    @Singleton
    @Provides
    fun provideLaunchApi(
        retrofit: Retrofit
    ): LaunchApiService {
        return retrofit.create(LaunchApiService::class.java)
    }
}