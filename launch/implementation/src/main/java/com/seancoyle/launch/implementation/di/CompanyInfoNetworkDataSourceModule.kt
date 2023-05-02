package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.CompanyInfoNetworkDataSource
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CompanyInfoNetworkDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsCompanyInfoNetworkDataSource(
        impl: CompanyInfoNetworkDataSourceImpl
    ): CompanyInfoNetworkDataSource
}