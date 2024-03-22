package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.usecase.CompanyInfoComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CompanyInfoComponentImpl @Inject constructor(
    private val getCompanyInfoFromCacheUseCase: GetCompanyInfoFromCacheUseCase,
    private val getCompanyInfoFromNetworkAndInsertToCacheUseCase: GetCompanyInfoFromNetworkAndInsertToCacheUseCase
): CompanyInfoComponent {
    override fun getCompanyInfoFromCacheUseCase(): Flow<DataResult<Company?>> {
        return getCompanyInfoFromCacheUseCase.invoke()
    }

    override fun getCompanyInfoFromNetworkAndInsertToCacheUseCase(): Flow<DataResult<Company>> {
        return getCompanyInfoFromNetworkAndInsertToCacheUseCase.invoke()
    }
}