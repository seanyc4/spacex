package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.DataResult
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.data.cache.CompanyCacheDataSource
import javax.inject.Inject

internal class InsertCompanyInfoToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource
) : InsertCompanyInfoToCacheUseCase {

    override suspend operator fun invoke(companyInfo: Company): DataResult<Long?> =
        cacheDataSource.insert(companyInfo)

}