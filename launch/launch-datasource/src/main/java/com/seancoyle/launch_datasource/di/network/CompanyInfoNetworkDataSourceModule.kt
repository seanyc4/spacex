package com.seancoyle.launch_datasource.di.network

import com.seancoyle.launch_datasource.network.CompanyInfoNetworkDataSource
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyInfoNetworkDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsCompanyInfoNetworkDataSource(
        impl: CompanyInfoNetworkDataSourceImpl
    ): CompanyInfoNetworkDataSource
}