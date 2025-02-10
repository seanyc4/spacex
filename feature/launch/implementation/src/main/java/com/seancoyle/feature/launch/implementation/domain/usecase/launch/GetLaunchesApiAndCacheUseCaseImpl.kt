package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.model.LaunchOptions
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetLaunchesApiAndCacheUseCaseImpl @Inject constructor(
    private val launchRepository: LaunchRepository,
    private val launchOptions: LaunchOptions,
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer
) : GetLaunchesApiAndCacheUseCase {

    override operator fun invoke(): Flow<Result<Unit, DataError>> = flow {
        emit(getLaunchesFromNetwork())
    }

    private suspend fun getLaunchesFromNetwork(): Result<Unit, DataError> {
        return when (val networkResult = launchRepository.getLaunchesApi(launchOptions)) {
            is Result.Success -> {
                val transformedLaunches = transformLaunchData(networkResult.data)
                cacheData(transformedLaunches)
            }
            is Result.Error -> Result.Error(networkResult.error)
        }
    }

    private suspend fun cacheData(launches: List<LaunchTypes.Launch>): Result<Unit, DataError> {
        return when (val cacheResult = launchRepository.insertLaunchesCache(launches)) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(cacheResult.error)
        }
    }

    private fun transformLaunchData(launches: List<LaunchTypes.Launch>): List<LaunchTypes.Launch> {
        return launches.map { launch ->
            val localDateTime = dateFormatter.formatDate(launch.launchDate)
            launch.copy(
                launchDate = dateTransformer.formatDateTimeToString(localDateTime),
                launchDateLocalDateTime = localDateTime,
                launchStatus = mapIsLaunchSuccessToStatus(launch.isLaunchSuccess),
                launchYear = dateTransformer.returnYearOfLaunch(localDateTime),
                launchDateStatus = mapLaunchDateToStatus(localDateTime),
                launchDays = dateTransformer.getLaunchDaysDifference(localDateTime)
            )
        }
    }

    private fun mapIsLaunchSuccessToStatus(isLaunchSuccess: Boolean?) =
        when (isLaunchSuccess) {
            true -> { LaunchStatus.SUCCESS }
            false -> { LaunchStatus.FAILED }
            else -> { LaunchStatus.UNKNOWN }
        }

    private fun mapLaunchDateToStatus(localDateTime: LocalDateTime) =
        if (dateTransformer.isPastLaunch(localDateTime)) {
            LaunchDateStatus.PAST
        } else {
            LaunchDateStatus.FUTURE
        }

}