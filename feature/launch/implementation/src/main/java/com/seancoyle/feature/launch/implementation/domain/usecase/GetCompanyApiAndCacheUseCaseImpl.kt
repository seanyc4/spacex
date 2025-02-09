package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyApiAndCacheUseCaseImpl @Inject constructor(
    private val companyRepository: CompanyRepository
) : GetCompanyApiAndCacheUseCase {

    override operator fun invoke(): Flow<Result<Unit, DataError>> = flow {
        emit(getCompanyFromNetwork())
    }

    private suspend fun getCompanyFromNetwork(): Result<Unit, DataError> {
        return when (val networkResult = companyRepository.getCompanyApi()) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(networkResult.error)
        }
    }
}