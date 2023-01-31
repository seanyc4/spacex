package com.seancoyle.launch_datasource.di.network

import com.seancoyle.launch_datasource.network.CompanyInfoApi
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkDataSource
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkDataSourceImpl
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkMapper
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