package com.seancoyle.feature.launch.implementation.domain.usecase.company

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyApiAndCacheUseCaseImpl @Inject constructor(
    private val companyRepository: CompanyRepository
) : GetCompanyApiAndCacheUseCase {

    override operator fun invoke(): Flow<LaunchResult<Unit, DataError>> = flow {
        emit(companyRepository.getCompanyApi())
    }

}