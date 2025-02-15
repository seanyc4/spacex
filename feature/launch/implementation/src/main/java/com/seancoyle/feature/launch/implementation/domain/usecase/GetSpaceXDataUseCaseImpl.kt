package com.seancoyle.feature.launch.implementation.domain.usecase

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.RemoteError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.usecase.company.GetCompanyApiAndCacheUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow

internal class GetSpaceXDataUseCaseImpl @Inject constructor(
    private val getCompanyApiAndCacheUseCase: GetCompanyApiAndCacheUseCase,
    private val getLaunchesApiAndCacheUseCase: GetLaunchesApiAndCacheUseCase
) : GetSpaceXDataUseCase {

    override operator fun invoke(): Flow<LaunchResult<Unit, DataError>> = flow {
        combine(
            getCompanyApiAndCacheUseCase(),
            getLaunchesApiAndCacheUseCase()
        ) { companyResult, launchesResult ->
            when {
                companyResult is LaunchResult.Success && launchesResult is LaunchResult.Success -> {
                    LaunchResult.Success(Unit)
                }

                companyResult is LaunchResult.Error -> {
                    LaunchResult.Error(companyResult.error)
                }

                launchesResult is LaunchResult.Error -> {
                    LaunchResult.Error(launchesResult.error)
                }

                else -> {
                    LaunchResult.Error(RemoteError.NETWORK_CONNECTION_FAILED)
                }
            }
        }.collect { combinedResult ->
            emit(combinedResult)
        }
    }
}