package com.seancoyle.launch.implementation.di

import com.seancoyle.launch.api.domain.usecase.CompanyInfoComponent
import com.seancoyle.launch.implementation.domain.usecase.CompanyInfoComponentImpl
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyApiAndCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyFromCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.GetCompanyFromCacheUseCaseImpl
import com.seancoyle.launch.implementation.domain.usecase.InsertCompanyInfoToCacheUseCase
import com.seancoyle.launch.implementation.domain.usecase.InsertCompanyInfoToCacheUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class CompanyInfoUseCaseModule {

    @Binds
    abstract fun bindsGetCompanyInfoFromNetworkAndInsertToCacheUseCase(
        impl: GetCompanyApiAndCacheUseCaseImpl
    ): GetCompanyApiAndCacheUseCase

    @Binds
    abstract fun bindsGetCompanyInfoFromCacheUseCase(
        impl: GetCompanyFromCacheUseCaseImpl
    ): GetCompanyFromCacheUseCase

    @Binds
    abstract fun bindsInsertCompanyInfoToCacheUseCase(
        impl: InsertCompanyInfoToCacheUseCaseImpl
    ): InsertCompanyInfoToCacheUseCase

    @Binds
    abstract fun bindsGetCompanyInfoComponent(
        impl: CompanyInfoComponentImpl
    ): CompanyInfoComponent

}