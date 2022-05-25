package com.seancoyle.spacex.di.data.network.companyinfo

import com.seancoyle.spacex.framework.datasource.api.company.FakeCompanyInfoApi
import com.seancoyle.spacex.framework.datasource.network.abstraction.company.CompanyInfoRetrofitService
import com.seancoyle.spacex.framework.datasource.network.company.FakeCompanyInfoRetrofitServiceImpl
import com.seancoyle.spacex.framework.datasource.network.mappers.company.CompanyInfoNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CompanyInfoNetworkRetrofitServiceModule::class]
)
object TestCompanyInfoRetrofitServiceModule {

    @Singleton
    @Provides
    fun provideCompanyInfoRetrofitService(
        fakeApi: FakeCompanyInfoApi,
        networkMapper: CompanyInfoNetworkMapper
    ): CompanyInfoRetrofitService {
        return FakeCompanyInfoRetrofitServiceImpl(
            fakeApi = fakeApi,
            networkMapper = networkMapper
        )
    }

}