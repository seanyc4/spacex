package com.seancoyle.launch_datasource.di.cache.companyinfo

import com.seancoyle.launch_datasource.cache.database.Database
import com.seancoyle.launch_datasource.cache.dao.company.CompanyInfoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoCacheDaoModule {

    @Singleton
    @Provides
    fun provideCompanyInfoDao(database: Database): CompanyInfoDao {
        return database.companyInfoDao()
    }

}