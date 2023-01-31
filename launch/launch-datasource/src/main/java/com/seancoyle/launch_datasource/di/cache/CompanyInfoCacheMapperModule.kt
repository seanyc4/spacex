package com.seancoyle.launch_datasource.di.cache

import com.seancoyle.launch_datasource.cache.CompanyInfoEntityMapper
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