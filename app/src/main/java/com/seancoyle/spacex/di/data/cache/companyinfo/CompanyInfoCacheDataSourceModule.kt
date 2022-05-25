package com.seancoyle.spacex.di.data.cache.companyinfo

import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.cache.implementation.company.CompanyInfoCacheDataSourceImpl
import com.seancoyle.spacex.framework.datasource.cache.abstraction.company.CompanyInfoDaoService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoCacheDataSourceModule {

    @Singleton
    @Provides
    fun provideCompanyInfoCacheDataSource(
        daoService: CompanyInfoDaoService
    ): CompanyInfoCacheDataSource {
        return CompanyInfoCacheDataSourceImpl(
            daoService = daoService
        )
    }
}