package com.seancoyle.spacex.di.data.network.companyinfo

import com.seancoyle.launch_datasource.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.launch_datasource.network.api.company.CompanyInfoApi
import com.seancoyle.launch_datasource.network.implementation.company.CompanyInfoNetworkDataSourceImpl
import com.seancoyle.launch_datasource.network.mappers.company.CompanyInfoNetworkMapper
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
        api: CompanyInfoApi,
        networkMapper: CompanyInfoNetworkMapper
    ): CompanyInfoNetworkDataSource {
        return CompanyInfoNetworkDataSourceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }
}