package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.domain.usecase.CompanyInfoComponent
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromNetworkAndInsertToCacheUseCase
import javax.inject.Inject

internal class CompanyInfoComponentImpl @Inject constructor(
    private val getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase,
    private val getCompanyInfoFromNetworkAndInsertToCacheUseCase: GetCompanyInfoFromNetworkAndInsertToCacheUseCase
): CompanyInfoComponent {
    override fun getCompanyInfoFromCacheUseCase(): GetCompanyInfoFromCacheUseCase {
        return getCompanyInfoFromCacheUseCase
    }

    override fun getCompanyInfoFromNetworkAndInsertToCacheUseCase(): GetCompanyInfoFromNetworkAndInsertToCacheUseCase {
        return getCompanyInfoFromNetworkAndInsertToCacheUseCase
    }
}