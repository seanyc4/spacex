package com.seancoyle.feature.launch.implementation.di.company

import com.seancoyle.feature.launch.implementation.data.remote.company.CompanyRemoteDataSourceImpl
import com.seancoyle.feature.launch.implementation.data.repository.company.CompanyRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CompanyRemoteDataSourceModule {

    @Binds
    abstract fun bindCompanyRemoteDataSource(
        impl: CompanyRemoteDataSourceImpl
    ): CompanyRemoteDataSource
}