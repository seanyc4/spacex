package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.api.domain.model.Company

internal interface InsertCompanyInfoToCacheUseCase {
    suspend operator fun invoke(companyInfo: Company): Result<Long?, DataError>
}