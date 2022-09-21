package com.seancoyle.spacex.di

import com.seancoyle.spacex.di.data.network.companyinfo.CompanyInfoApiModule
import com.seancoyle.core_testing.JsonFileReader
import com.seancoyle.spacex.datasource.network.company.FakeCompanyInfoApi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [CompanyInfoApiModule::class]
)
object TestCompanyInfoApiModule {

    @Singleton
    @Provides
    fun provideFakeCompanyInfoApi(
        jsonFileReader: JsonFileReader
    ): FakeCompanyInfoApi {
        return FakeCompanyInfoApi(
            jsonFileReader = jsonFileReader
        )
    }

}