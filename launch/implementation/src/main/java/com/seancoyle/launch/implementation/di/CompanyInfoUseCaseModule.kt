package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.domain.usecase.CompanyInfoComponent
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.implementation.domain.CompanyInfoComponentImpl
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

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

    @Binds
    abstract fun bindsGetCompanyInfoComponent(
        impl: CompanyInfoComponentImpl
    ): CompanyInfoComponent

}