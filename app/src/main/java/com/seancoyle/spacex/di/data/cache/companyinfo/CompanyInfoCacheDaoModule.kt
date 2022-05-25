package com.seancoyle.spacex.di.data.cache.companyinfo

import com.seancoyle.spacex.framework.datasource.cache.database.Database
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
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