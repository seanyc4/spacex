package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.Company
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyCacheUseCaseImpl @Inject constructor(
    private val companyRepository: CompanyRepository
) : GetCompanyCacheUseCase {

    override operator fun invoke(): Flow<Result<Company?, DataError>> = flow {
        emit(companyRepository.getCompanyFromCache())
    }
}