package com.seancoyle.spacex.di.data.cache.companyinfo

import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.database.daos.CompanyInfoDao
import com.seancoyle.launch_datasource.cache.implementation.company.CompanyInfoCacheDataSourceImpl
import com.seancoyle.launch_datasource.cache.mappers.company.CompanyInfoEntityMapper
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
        dao: CompanyInfoDao,
        companyInfoEntityMapper: CompanyInfoEntityMapper
    ): CompanyInfoCacheDataSource {
        return CompanyInfoCacheDataSourceImpl(
            dao = dao,
            entityMapper = companyInfoEntityMapper
        )
    }
}