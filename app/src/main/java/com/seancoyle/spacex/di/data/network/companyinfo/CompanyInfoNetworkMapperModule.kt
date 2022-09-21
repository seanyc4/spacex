package com.seancoyle.spacex.di.data.network.companyinfo

import com.seancoyle.launch_datasource.network.abstraction.numberformatter.NumberFormatter
import com.seancoyle.launch_datasource.network.mappers.company.CompanyInfoNetworkMapper
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