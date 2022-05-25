package com.seancoyle.spacex.di.data.network.companyinfo

import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.data.network.implementation.company.CompanyInfoNetworkDataSourceImpl
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoNetworkDataSourceModule {

    @Singleton
    @Provides
    fun provideCompanyInfoNetworkDataSource(
        companyInfoRetrofitService: CompanyInfoRetrofitService
    ): CompanyInfoNetworkDataSource {
        return CompanyInfoNetworkDataSourceImpl(
            retrofitService = companyInfoRetrofitService
        )
    }
}