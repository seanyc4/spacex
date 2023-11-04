package com.seancoyle.spacex.di.data.companyinfo

import com.seancoyle.launch.api.data.CompanyInfoNetworkDataSource
import com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
import com.seancoyle.launch.implementation.di.CompanyInfoNetworkDataSourceModule
import com.seancoyle.launch.implementation.network.company.FakeCompanyInfoApi
import com.seancoyle.launch.implementation.network.company.FakeCompanyInfoNetworkDataSourceImpl
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
        fakeApi: com.seancoyle.launch.implementation.network.company.FakeCompanyInfoApi,
        networkMapper: CompanyInfoNetworkMapper
    ): CompanyInfoNetworkDataSource {
        return com.seancoyle.launch.implementation.network.company.FakeCompanyInfoNetworkDataSourceImpl(
            fakeApi = fakeApi,
            networkMapper = networkMapper
        )
    }

}