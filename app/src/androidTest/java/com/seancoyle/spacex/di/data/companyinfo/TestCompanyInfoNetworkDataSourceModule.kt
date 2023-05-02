package com.seancoyle.spacex.di.data.companyinfo

import com.seancoyle.spacex.framework.datasource.network.company.FakeCompanyInfoApi
import com.seancoyle.spacex.framework.datasource.network.company.FakeCompanyInfoNetworkDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [com.seancoyle.launch.implementation.di.CompanyInfoNetworkDataSourceModule::class]
)
object TestCompanyInfoNetworkDataSourceModule {

    @Singleton
    @Provides
    fun provideCompanyInfoNetworkDataSource(
        fakeApi: FakeCompanyInfoApi,
        networkMapper: com.seancoyle.launch.implementation.data.network.CompanyInfoNetworkMapper
    ): com.seancoyle.launch.api.CompanyInfoNetworkDataSource {
        return FakeCompanyInfoNetworkDataSourceImpl(
            fakeApi = fakeApi,
            networkMapper = networkMapper
        )
    }

}