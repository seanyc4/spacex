package com.seancoyle.spacex.di.data.companyinfo

import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
import com.seancoyle.launch.implementation.di.CompanyInfoNetworkDataSourceModule
import com.seancoyle.spacex.data.network.company.FakeCompanyInfoApi
import com.seancoyle.spacex.data.network.company.FakeCompanyInfoNetworkDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CompanyInfoNetworkDataSourceModule::class]
)
object TestCompanyInfoNetworkDataSourceModule {

    @Singleton
    @Provides
    fun provideCompanyInfoNetworkDataSource(
        fakeApi: FakeCompanyInfoApi,
        networkMapper: CompanyInfoNetworkMapper
    ): CompanyInfoNetworkDataSource {
        return FakeCompanyInfoNetworkDataSourceImpl(
            fakeApi = fakeApi,
            networkMapper = networkMapper
        )
    }

}