package com.seancoyle.launch_datasource.di.network

import com.seancoyle.core.util.NumberFormatter
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoNetworkMapperModule {

    @Singleton
    @Provides
    fun provideCompanyInfoNetworkMapper(
        numberFormatter: NumberFormatter
    ): CompanyInfoNetworkMapper {
        return CompanyInfoNetworkMapper(
            numberFormatter = numberFormatter
        )
    }
}