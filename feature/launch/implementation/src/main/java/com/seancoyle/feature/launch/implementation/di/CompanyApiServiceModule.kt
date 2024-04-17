package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.network.CompanyApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CompanyApiServiceModule {

    @Singleton
    @Provides
    fun provideCompanyApi(
        retrofit: Retrofit
    ): CompanyApiService {
        return retrofit.create(CompanyApiService::class.java)
    }

}