package com.seancoyle.spacex.di.data.cache.companyinfo

import com.seancoyle.launch_datasource.cache.mappers.company.CompanyInfoEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CompanyInfoCacheMapperModule {

    @Singleton
    @Provides
    fun provideCompanyInfoCacheMapper(): CompanyInfoEntityMapper {
        return CompanyInfoEntityMapper()
    }

}