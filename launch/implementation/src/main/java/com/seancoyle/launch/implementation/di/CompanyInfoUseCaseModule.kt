package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.implementation.domain.usecase.CompanyInfoComponent
import com.seancoyle.launch.implementation.domain.usecase.CompanyInfoComponentImpl
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyInfoFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCaseImpl
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