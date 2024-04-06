package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSourceImpl
import com.seancoyle.launch.implementation.domain.cache.CompanyCacheDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyCacheDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsCompanyCacheDataSource(
        impl: CompanyCacheDataSourceImpl
    ): CompanyCacheDataSource
}