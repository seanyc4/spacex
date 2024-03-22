package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.network.ApiResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.usecase.CompanyInfoComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CompanyInfoComponentImpl @Inject constructor(
    private val getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase,
    private val getCompanyInfoFromNetworkAndInsertToCacheUseCase: GetCompanyInfoFromNetworkAndInsertToCacheUseCase
): CompanyInfoComponent {
    override fun getCompanyInfoFromCacheUseCase(): Flow<Company?> {
        return getCompanyInfoFromCacheUseCase.invoke()
    }

    override fun getCompanyInfoFromNetworkAndInsertToCacheUseCase(): Flow<ApiResult<Company>> {
        return getCompanyInfoFromNetworkAndInsertToCacheUseCase.invoke()
    }
}