package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.usecase.CompanyInfoComponent
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class CompanyInfoComponentImpl @Inject constructor(
    private val getCompanyFromCacheUseCase: GetCompanyFromCacheUseCase,
    private val getCompanyApiAndCacheUseCase: GetCompanyApiAndCacheUseCase
): CompanyInfoComponent {
    override fun getCompanyInfoFromCacheUseCase(): Flow<DataResult<Company?>> {
        return getCompanyFromCacheUseCase.invoke()
    }

    override fun getCompanyInfoApiAndCacheUseCase(): Flow<DataResult<Company>> {
        return getCompanyApiAndCacheUseCase.invoke()
    }
}