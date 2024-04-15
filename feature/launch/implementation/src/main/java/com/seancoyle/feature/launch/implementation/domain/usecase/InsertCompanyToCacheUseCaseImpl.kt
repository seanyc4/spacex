package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.cache.CompanyCacheDataSource
import javax.inject.Inject

internal class InsertCompanyToCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: CompanyCacheDataSource
) : InsertCompanyToCacheUseCase {

    override suspend operator fun invoke(companyInfo: Company): Result<Long?, DataError> =
        cacheDataSource.insert(companyInfo)
}