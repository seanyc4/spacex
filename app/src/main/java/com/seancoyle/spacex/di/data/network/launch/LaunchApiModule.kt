package com.seancoyle.spacex.di.data.network.launch

import com.seancoyle.spacex.framework.datasource.network.api.launch.LaunchApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LaunchApiModule {

    @Singleton
    @Provides
    fun provideLaunchApi(
        retrofit: Retrofit
    ): LaunchApi {
        return retrofit.create(LaunchApi::class.java)
    }
}