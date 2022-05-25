package com.seancoyle.spacex.di.data.network.companyinfo

import com.seancoyle.spacex.framework.datasource.api.company.FakeCompanyInfoApi
import com.seancoyle.spacex.util.JsonFileReader
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