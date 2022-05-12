package com.seancoyle.spacex.di

import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.spacex.business.domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.business.interactors.company.*
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [CompanyInfoInteractorsModule::class]
)
object TestCompanyInfoInteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideCompanyInfoInteractors(
        companyInfoDataSource: CompanyInfoCacheDataSource,
        companyInfoNetworkDataSource: CompanyInfoNetworkDataSource,
        companyInfoFactory: CompanyInfoFactory
    ): CompanyInfoInteractors {
        return CompanyInfoInteractors(
            getCompanyInfoFromNetworkAndInsertToCache = GetCompanyInfoFromNetworkAndInsertToCache(
                cacheDataSource = companyInfoDataSource,
                networkDataSource = companyInfoNetworkDataSource,
                factory = companyInfoFactory
            ),
            getCompanyInfoFromCache = GetCompanyInfoFromCache(
                cacheDataSource = companyInfoDataSource
            )
        )
    }

}