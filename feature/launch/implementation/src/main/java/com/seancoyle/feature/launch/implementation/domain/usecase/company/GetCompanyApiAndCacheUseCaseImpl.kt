package com.seancoyle.feature.launch.implementation.domain.usecase.company

import com.seancoyle.core.common.result.DataSourceError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.implementation.domain.repository.CompanyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetCompanyApiAndCacheUseCaseImpl @Inject constructor(
    private val companyRepository: CompanyRepository
) : GetCompanyApiAndCacheUseCase {

    override operator fun invoke(): Flow<LaunchResult<Unit, DataSourceError>> = flow {
        emit(getCompanyFromNetwork())
    }

    private suspend fun getCompanyFromNetwork(): LaunchResult<Unit, DataSourceError> {
        return when (val networkResult = companyRepository.getCompanyApi()) {
            is LaunchResult.Success -> LaunchResult.Success(Unit)
            is LaunchResult.Error -> LaunchResult.Error(networkResult.error)
        }
    }
}