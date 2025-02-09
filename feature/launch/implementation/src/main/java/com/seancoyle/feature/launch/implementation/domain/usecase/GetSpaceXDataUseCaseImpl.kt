package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

internal class GetSpaceXDataUseCaseImpl @Inject constructor(
    private val getCompanyApiAndCacheUseCase: GetCompanyApiAndCacheUseCase,
    private val getLaunchesApiAndCacheUseCase: GetLaunchesApiAndCacheUseCase
) : GetSpaceXDataUseCase {

    override operator fun invoke(): Flow<Result<Unit, DataError>> = flow {
        combine(
            getCompanyApiAndCacheUseCase(),
            getLaunchesApiAndCacheUseCase()
        ) { companyResult, launchesResult ->
            when {
                companyResult is Result.Success && launchesResult is Result.Success -> {
                    Result.Success(Unit)
                }

                companyResult is Result.Error -> {
                    Result.Error(companyResult.error)
                }

                launchesResult is Result.Error -> {
                    Result.Error(launchesResult.error)
                }

                else -> {
                    Result.Error(DataError.NETWORK_CONNECTION_FAILED)
                }
            }
        }.collect { combinedResult ->
            emit(combinedResult)
        }
    }
}