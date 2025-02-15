package com.seancoyle.feature.launch.implementation.domain.usecase.company

import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyCacheUseCaseImpl @Inject constructor(
    private val companyRepository: CompanyRepository
) : GetCompanyCacheUseCase {

    override operator fun invoke(): Flow<LaunchResult<Company?, LocalError>> = flow {
        emit(companyRepository.getCompanyCache())
    }
}