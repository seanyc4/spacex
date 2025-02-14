package com.seancoyle.feature.launch.implementation.di.company

import com.seancoyle.feature.launch.implementation.data.cache.company.CompanyLocalDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyDiskDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyDiskDataSourceModule {

    @Binds
    abstract fun bindsCompanyDiskDataSource(
        impl: CompanyLocalDataSourceImpl
    ): CompanyDiskDataSource
}