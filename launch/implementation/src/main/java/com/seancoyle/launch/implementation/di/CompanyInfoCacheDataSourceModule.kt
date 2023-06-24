package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.implementation.data.cache.CompanyInfoCacheDataSourceImpl
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