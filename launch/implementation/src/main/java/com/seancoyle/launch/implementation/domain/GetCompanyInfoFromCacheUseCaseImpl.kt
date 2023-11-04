package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.data.CompanyCacheDataSource
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.api.domain.usecase.GetCompanyInfoFromCacheUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyInfoFromCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource
) : GetCompanyInfoFromCacheUseCase {

    override operator fun invoke(): Flow<Company?> = flow {
       emit(cacheDataSource.getCompany())
    }

}