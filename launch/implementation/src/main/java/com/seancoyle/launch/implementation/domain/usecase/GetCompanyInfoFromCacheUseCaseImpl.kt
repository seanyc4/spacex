package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSource
import com.seancoyle.launch.implementation.domain.model.Company
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