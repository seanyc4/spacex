package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.network.CompanyNetworkDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.network.CompanyNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyInfoNetworkDataSourceModule {

    @Binds
    abstract fun bindsCompanyInfoNetworkDataSource(
        impl: CompanyNetworkDataSourceImpl
    ): CompanyNetworkDataSource
}