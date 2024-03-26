package com.seancoyle.core_database.implementation.di

import com.seancoyle.core_database.api.CompanyDao
import com.seancoyle.core_database.implementation.Database
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