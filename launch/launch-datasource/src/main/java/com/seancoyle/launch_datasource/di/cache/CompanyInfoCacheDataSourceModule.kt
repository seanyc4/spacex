package com.seancoyle.launch_datasource.di.cache

import com.seancoyle.launch_datasource.cache.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.cache.CompanyInfoCacheDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyInfoCacheDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsCompanyInfoCacheDataSource(
        impl: CompanyInfoCacheDataSourceImpl
    ): CompanyInfoCacheDataSource
}