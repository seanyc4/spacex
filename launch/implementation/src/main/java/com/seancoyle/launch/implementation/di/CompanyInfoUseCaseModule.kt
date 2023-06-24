package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.implementation.domain.CompanyInfoUseCases
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class CompanyInfoUseCaseModule {

    @Binds
    abstract fun bindsGetCompanyInfoFromNetworkAndInsertToCacheUseCase(
        impl: GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl
    ): GetCompanyInfoFromNetworkAndInsertToCacheUseCase

    @Binds
    abstract fun bindsGetCompanyInfoFromCacheUseCase(
        impl: GetCompanyInfoFromCacheUseCaseImpl
    ): GetCompanyInfoFromCacheUseCase

    companion object {
        @ViewModelScoped
        @Provides
        fun provideCompanyInfoUseCases(
            getCompanyInfoFromNetworkAndInsertToCacheUseCase: GetCompanyInfoFromNetworkAndInsertToCacheUseCase,
            getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase
        ): CompanyInfoUseCases {
            return CompanyInfoUseCases(
                getCompanyInfoFromNetworkAndInsertToCacheUseCase = getCompanyInfoFromNetworkAndInsertToCacheUseCase,
                getCompanyInfoFromCacheUseCase = getCompanyInfoFromCacheUseCase
            )
        }
    }
}