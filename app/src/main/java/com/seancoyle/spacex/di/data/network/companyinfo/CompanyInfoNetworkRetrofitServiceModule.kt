package com.seancoyle.spacex.di.data.network.companyinfo

import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import com.seancoyle.spacex.framework.datasource.network.api.company.CompanyInfoApi
import com.seancoyle.spacex.framework.datasource.network.implementation.company.CompanyInfoRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.company.CompanyInfoNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoNetworkRetrofitServiceModule {

    @Singleton
    @Provides
    fun provideCompanyInfoRetrofitService(
        api: CompanyInfoApi,
        networkMapper: CompanyInfoNetworkMapper
    ): CompanyInfoRetrofitService {
        return CompanyInfoRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }
}