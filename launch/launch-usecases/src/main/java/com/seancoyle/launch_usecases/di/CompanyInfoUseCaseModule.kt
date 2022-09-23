package com.seancoyle.launch_usecases.di

import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.launch_usecases.company.CompanyInfoUseCases
import com.seancoyle.launch_usecases.company.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch_usecases.company.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CompanyInfoUseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideCompanyInfoUseCases(
        companyInfoDataSource: CompanyInfoCacheDataSource,
        companyInfoNetworkDataSource: CompanyInfoNetworkDataSource,
        companyInfoFactory: CompanyInfoFactory
    ): CompanyInfoUseCases {
        return CompanyInfoUseCases(
            getCompanyInfoFromNetworkAndInsertToCacheUseCase = GetCompanyInfoFromNetworkAndInsertToCacheUseCase(
                cacheDataSource = companyInfoDataSource,
                networkDataSource = companyInfoNetworkDataSource,
                factory = companyInfoFactory
            ),
            getCompanyInfoFromCacheUseCase = GetCompanyInfoFromCacheUseCase(
                cacheDataSource = companyInfoDataSource
            )
        )
    }

}