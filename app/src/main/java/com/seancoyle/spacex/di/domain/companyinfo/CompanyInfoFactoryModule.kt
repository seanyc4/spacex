package com.seancoyle.spacex.di.domain.companyinfo

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoFactoryModule {

    @Singleton
    @Provides
    fun provideCompanyInfoFactory(): CompanyInfoFactory {
        return CompanyInfoFactory()
    }

}