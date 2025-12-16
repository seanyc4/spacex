package com.seancoyle.feature.launch.implementation.domain.usecase.launch

import com.seancoyle.core.common.dataformatter.DateFormatter
import com.seancoyle.core.common.dataformatter.DateTransformer
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.datastore.api.DataStorePreferencesComponent
import com.seancoyle.feature.launch.api.LaunchConstants.PAGINATION_LIMIT
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchTypes
import com.seancoyle.feature.launch.implementation.domain.repository.LaunchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

private const val TAG = "PaginateLaunchesUseCase"
private const val CURRENT_PAGE = "current_page"

internal class PaginateLaunchesUseCase @Inject constructor(
    private val launchRepository: LaunchRepository,
    private val dateFormatter: DateFormatter,
    private val dateTransformer: DateTransformer,
    private val dataStorePreferencesComponent: DataStorePreferencesComponent
) {

    operator fun invoke(): Flow<LaunchResult<PaginationResult, DataError>> = flow {

        val page = dataStorePreferencesComponent.getInt(CURRENT_PAGE, 0)
        Timber.tag(TAG).d("Current page: $page")

        val offset = page * PAGINATION_LIMIT
        Timber.tag(TAG).d("Current offset: $offset")

        when (val result = launchRepository.getLaunchesApi(offset)) {
            is LaunchResult.Success -> {
                val launches = result.data
                val transformed = transformLaunchData(launches)

                when (val cacheResult = launchRepository.insertLaunchesCache(transformed)) {
                    is LaunchResult.Success -> {
                        val fetchedCount = launches.size
                        Timber.tag(TAG).d("Page=$page fetchedCount=$fetchedCount")

                        if (fetchedCount < PAGINATION_LIMIT) {
                            Timber.tag(TAG).d("End of pagination reached at page=$page")
                            emit(LaunchResult.Success(PaginationResult.EndReached))
                        } else {
                            val nextPage = page + 1
                            dataStorePreferencesComponent.saveInt(CURRENT_PAGE, nextPage)
                            emit(LaunchResult.Success(PaginationResult.Success(nextPage)))
                        }
                    }

                    is LaunchResult.Error -> {
                        Timber.tag(TAG).e("Cache error on page=$page: ${cacheResult.error}")
                        emit(LaunchResult.Error(cacheResult.error))
                    }
                }
            }

            is LaunchResult.Error -> {
                emit(LaunchResult.Error(result.error))
            }
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
