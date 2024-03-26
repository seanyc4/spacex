package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.implementation.domain.network.CompanyInfoNetworkDataSource
import com.seancoyle.launch.implementation.network.company.FakeCompanyInfoNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CompanyInfoNetworkDataSourceModule::class]
)
internal abstract class FakeCompanyNetworkDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsFakeCompanyNetworkDataSource(
        impl: FakeCompanyInfoNetworkDataSourceImpl
    ): CompanyInfoNetworkDataSource
}