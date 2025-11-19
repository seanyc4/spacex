package com.seancoyle.feature.launch.implementation.di.company

import com.seancoyle.feature.launch.implementation.data.local.company.CompanyLocalDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyLocalDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyLocalDataSourceModule {

    @Binds
    abstract fun bindCompanyLocalDataSource(
        impl: CompanyLocalDataSourceImpl
    ): CompanyLocalDataSource
}
