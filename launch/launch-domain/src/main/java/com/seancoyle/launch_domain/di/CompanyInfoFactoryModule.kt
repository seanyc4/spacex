package com.seancoyle.launch_domain.di

import com.seancoyle.launch_domain.model.company.CompanyInfoFactory
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