package com.seancoyle.launch.implementation.domain.usecase

internal interface CompanyInfoComponent {
    fun getCompanyInfoFromCacheUseCase(): GetCompanyInfoFromCacheUseCase
    fun getCompanyInfoFromNetworkAndInsertToCacheUseCase(): GetCompanyInfoFromNetworkAndInsertToCacheUseCase
}