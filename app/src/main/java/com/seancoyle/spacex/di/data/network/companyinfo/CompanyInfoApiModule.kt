package com.seancoyle.spacex.di.data.network.companyinfo

import com.seancoyle.launch_datasource.network.api.company.CompanyInfoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoApiModule {

    @Singleton
    @Provides
    fun provideCompanyInfoApi(
        retrofit: Retrofit
    ): CompanyInfoApi {
        return retrofit.create(CompanyInfoApi::class.java)
    }

}