package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.DataError.LocalError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetLaunchesApiAndCacheUseCaseImpl @Inject constructor(
    private val launchRepository: LaunchRepository,
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer
) : GetLaunchesApiAndCacheUseCase {

    override operator fun invoke(
        currentPage: Int
    ): Flow<LaunchResult<Unit, DataError>> = flow {
        emit(getLaunchesFromNetwork(currentPage))
    }

    private suspend fun getLaunchesFromNetwork(currentPage: Int): LaunchResult<Unit, DataError> {
        val offset = currentPage * PAGINATION_LIMIT
        return when (val networkResult = launchRepository.getLaunchesApi(offset)) {
            is LaunchResult.Success -> {
                val transformedLaunches = transformLaunchData(networkResult.data)
                cacheData(transformedLaunches)
            }
            is LaunchResult.Error -> LaunchResult.Error(networkResult.error)
        }
    }

    private suspend fun cacheData(launches: List<LaunchTypes.Launch>): LaunchResult<Unit, LocalError> {
        return when (val cacheResult = launchRepository.insertLaunchesCache(launches)) {
            is LaunchResult.Success -> LaunchResult.Success(Unit)
            is LaunchResult.Error -> LaunchResult.Error(cacheResult.error)
        }
    }

    private fun transformLaunchData(launches: List<LaunchTypes.Launch>): List<LaunchTypes.Launch> {
        return launches.map { launch ->
            val localDateTime = dateFormatter.formatDate(launch.launchDate)
            launch.copy(
                launchDate = dateTransformer.formatDateTimeToString(localDateTime),
                launchDateLocalDateTime = localDateTime,
                launchYear = dateTransformer.returnYearOfLaunch(localDateTime),
                launchDateStatus = mapLaunchDateToStatus(localDateTime),
                launchDays = dateTransformer.getLaunchDaysDifference(localDateTime)
            )
        }
    }

    private fun mapLaunchDateToStatus(localDateTime: LocalDateTime) =
        if (dateTransformer.isPastLaunch(localDateTime)) {
            LaunchDateStatus.PAST
        } else {
            LaunchDateStatus.FUTURE
        }
}
