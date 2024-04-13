package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkDataSourceImpl
import com.seancoyle.launch.implementation.domain.network.CompanyInfoNetworkDataSource
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