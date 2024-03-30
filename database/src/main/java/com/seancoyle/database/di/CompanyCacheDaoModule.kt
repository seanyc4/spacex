package com.seancoyle.database.di

import com.seancoyle.database.Database
import com.seancoyle.database.dao.CompanyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object CompanyCacheDaoModule {

    @Singleton
    @Provides
    fun provideCompanyInfoDao(database: Database): CompanyDao {
        return database.companyInfoDao()
    }
}