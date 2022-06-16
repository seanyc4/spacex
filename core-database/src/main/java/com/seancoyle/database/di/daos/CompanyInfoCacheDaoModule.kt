package com.seancoyle.database.di.daos

import com.seancoyle.database.Database
import com.seancoyle.database.daos.CompanyInfoDao
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