package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.domain.DataError
import com.seancoyle.core.domain.DataResult
import com.seancoyle.launch.api.domain.model.Company

internal interface InsertCompanyInfoToCacheUseCase {
    suspend operator fun invoke(companyInfo: Company): DataResult<Long?, DataError>
}