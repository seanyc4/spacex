package com.seancoyle.feature.launch.implementation.di.company

import com.seancoyle.feature.launch.implementation.data.network.company.CompanyNetworkDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyNetworkDataSourceModule {

    @Binds
    abstract fun bindsCompanyInfoNetworkDataSource(
        impl: CompanyNetworkDataSourceImpl
    ): CompanyNetworkDataSource
}