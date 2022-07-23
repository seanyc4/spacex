package com.seancoyle.launch_datasource.di

import com.seancoyle.launch_datasource.di.network.companyinfo.CompanyInfoApiModule
import com.seancoyle.core_testing.JsonFileReader
import com.seancoyle.launch_datasource.network.company.FakeCompanyInfoApi
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
    ): com.seancoyle.launch_datasource.network.company.FakeCompanyInfoApi {
        return com.seancoyle.launch_datasource.network.company.FakeCompanyInfoApi(
            jsonFileReader = jsonFileReader
        )
    }

}