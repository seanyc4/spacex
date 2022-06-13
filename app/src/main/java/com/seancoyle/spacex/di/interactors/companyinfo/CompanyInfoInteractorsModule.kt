package com.seancoyle.spacex.di.interactors.companyinfo

import com.seancoyle.launch_datasource.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.launch_datasource.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.launch_domain.model.company.CompanyInfoFactory
import com.seancoyle.launch_interactors.company.CompanyInfoInteractors
import com.seancoyle.launch_interactors.company.GetCompanyInfoFromCache
import com.seancoyle.launch_interactors.company.GetCompanyInfoFromNetworkAndInsertToCache
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object CompanyInfoInteractorsModule {

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