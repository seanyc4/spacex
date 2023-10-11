package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.data.CompanyInfoCacheDataSource
import com.seancoyle.launch.api.domain.model.CompanyInfo
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCompanyInfoFromCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyInfoCacheDataSource
) : GetCompanyInfoFromCacheUseCase {

    override operator fun invoke(): Flow<CompanyInfo?> = flow {
       emit(cacheDataSource.getCompanyInfo())
    }

}