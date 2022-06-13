package com.seancoyle.spacex.di.interactors.companyinfo

import com.seancoyle.spacex.business.data.cache.abstraction.company.CompanyInfoCacheDataSource
import com.seancoyle.spacex.business.data.network.abstraction.company.CompanyInfoNetworkDataSource
import com.seancoyle.launch_domain.model.company.CompanyInfoFactory
import com.seancoyle.spacex.business.interactors.company.CompanyInfoInteractors
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromCache
import com.seancoyle.spacex.business.interactors.company.GetCompanyInfoFromNetworkAndInsertToCache
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