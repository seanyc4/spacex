package com.seancoyle.launch_usecases.di

import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch_datasource.cache.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.network.CompanyInfoNetworkDataSource
import com.seancoyle.launch_models.model.company.CompanyInfoFactory
import com.seancoyle.launch_usecases.company.CompanyInfoUseCases
import com.seancoyle.launch_usecases.company.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch_usecases.company.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object CompanyInfoUseCaseModule {

    @ViewModelScoped
    @Provides
    fun provideCompanyInfoUseCases(
        @IODispatcher ioDispatcher: CoroutineDispatcher,
        companyInfoDataSource: CompanyInfoCacheDataSource,
        companyInfoNetworkDataSource: CompanyInfoNetworkDataSource,
        companyInfoFactory: CompanyInfoFactory
    ): CompanyInfoUseCases {
        return CompanyInfoUseCases(
            getCompanyInfoFromNetworkAndInsertToCacheUseCase = GetCompanyInfoFromNetworkAndInsertToCacheUseCase(
                ioDispatcher = ioDispatcher,
                cacheDataSource = companyInfoDataSource,
                networkDataSource = companyInfoNetworkDataSource,
                factory = companyInfoFactory
            ),
            getCompanyInfoFromCacheUseCase = GetCompanyInfoFromCacheUseCase(
                ioDispatcher = ioDispatcher,
                cacheDataSource = companyInfoDataSource
            )
        )
    }

}