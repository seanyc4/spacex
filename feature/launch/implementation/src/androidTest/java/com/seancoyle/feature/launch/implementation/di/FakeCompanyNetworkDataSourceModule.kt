package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.domain.network.CompanyInfoNetworkDataSource
import com.seancoyle.feature.launch.implementation.network.company.FakeCompanyInfoNetworkDataSourceImpl
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