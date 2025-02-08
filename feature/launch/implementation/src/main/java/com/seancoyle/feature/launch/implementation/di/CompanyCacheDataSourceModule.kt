package com.seancoyle.feature.launch.implementation.di

import com.seancoyle.feature.launch.implementation.data.cache.CompanyCacheDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.cache.CompanyCacheDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyCacheDataSourceModule {

    @Binds
    abstract fun bindsCompanyCacheDataSource(
        impl: CompanyCacheDataSourceImpl
    ): CompanyCacheDataSource
}