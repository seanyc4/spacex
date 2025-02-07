package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.repository.SpaceXRepository
import javax.inject.Inject

internal class InsertCompanyToCacheUseCaseImpl @Inject constructor(
    private val spaceXRepository: SpaceXRepository
) : InsertCompanyToCacheUseCase {

    override suspend operator fun invoke(companyInfo: Company): Result<Long?, DataError> =
        spaceXRepository.insertCompany(companyInfo)
}