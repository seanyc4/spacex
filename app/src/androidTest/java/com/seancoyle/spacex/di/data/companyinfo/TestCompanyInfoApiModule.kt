package com.seancoyle.spacex.di.data.companyinfo

import com.seancoyle.core.testing.JsonFileReader
import com.seancoyle.launch.implementation.di.CompanyInfoApiModule
import com.seancoyle.spacex.data.network.company.FakeCompanyInfoApi
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