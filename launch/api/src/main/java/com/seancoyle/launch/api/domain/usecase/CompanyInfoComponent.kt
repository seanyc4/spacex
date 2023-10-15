package com.seancoyle.launch.api.domain.usecase

interface CompanyInfoComponent {
    fun getCompanyInfoFromCacheUseCase(): GetCompanyInfoFromCacheUseCase
    fun getCompanyInfoFromNetworkAndInsertToCacheUseCase(): GetCompanyInfoFromNetworkAndInsertToCacheUseCase
}