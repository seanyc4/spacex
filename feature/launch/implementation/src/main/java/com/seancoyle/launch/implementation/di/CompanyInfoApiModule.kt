package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.implementation.data.network.CompanyApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CompanyInfoApiModule {

    @Singleton
    @Provides
    fun provideCompanyInfoApi(
        retrofit: Retrofit
    ): CompanyApi {
        return retrofit.create(CompanyApi::class.java)
    }

}