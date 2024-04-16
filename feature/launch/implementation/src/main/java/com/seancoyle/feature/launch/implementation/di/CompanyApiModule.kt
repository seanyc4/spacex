package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.network.CompanyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CompanyApiModule {

    @Singleton
    @Provides
    fun provideCompanyApi(
        retrofit: Retrofit
    ): CompanyApi {
        return retrofit.create(CompanyApi::class.java)
    }

}