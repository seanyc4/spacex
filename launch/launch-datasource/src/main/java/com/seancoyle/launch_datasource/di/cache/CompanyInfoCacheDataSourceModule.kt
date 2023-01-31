package com.seancoyle.launch_datasource.di.cache

import com.seancoyle.database.daos.CompanyInfoDao
import com.seancoyle.launch_datasource.cache.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.cache.CompanyInfoCacheDataSourceImpl
import com.seancoyle.launch_datasource.cache.CompanyInfoEntityMapper
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