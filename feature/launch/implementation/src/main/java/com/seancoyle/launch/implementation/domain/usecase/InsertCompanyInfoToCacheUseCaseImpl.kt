package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.api.domain.model.Company
import com.seancoyle.launch.implementation.domain.cache.CompanyCacheDataSource
import javax.inject.Inject

internal class InsertCompanyInfoToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource
) : InsertCompanyInfoToCacheUseCase {

    override suspend operator fun invoke(companyInfo: Company): Result<Long?, DataError> =
        cacheDataSource.insert(companyInfo)

}