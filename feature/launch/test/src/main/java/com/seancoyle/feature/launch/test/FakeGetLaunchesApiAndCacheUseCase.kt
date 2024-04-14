package com.seancoyle.feature.launch.test

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.Result
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchStatus
import com.seancoyle.launch.api.domain.model.LaunchTypes
import com.seancoyle.launch.api.domain.model.Links
import com.seancoyle.launch.api.domain.model.Rocket
import com.seancoyle.launch.api.domain.usecase.GetLaunchesApiAndCacheUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class FakeGetLaunchesApiAndCacheUseCase : GetLaunchesApiAndCacheUseCase {

    val isSuccess = true
    override fun invoke(): Flow<Result<List<LaunchTypes.Launch>, DataError>> = flow {
        emit(getFakeLaunchesFromNetwork())
    }

    private fun getFakeLaunchesFromNetwork(): Result<List<LaunchTypes.Launch>, DataError> {
        // Simulating network data fetching behavior
        return if (shouldSimulateSuccess(isSuccess)) {
            // Simulate a successful data fetch
            Result.Success(
                listOf(
                    LaunchTypes.Launch(
                        id = "5",
                        launchDate = "2024-01-01",
                        launchDateLocalDateTime = LocalDateTime.now(),
                        launchYear = "2024",
                        launchStatus = LaunchStatus.SUCCESS,
                        links = Links(
                            missionImage = "https://example.com/mission3.jpg",
                            articleLink = "https://example.com/article3",
                            webcastLink = "https://example.com/webcast3",
                            wikiLink = "https://example.com/wiki3"
                        ),
                        missionName = "Starlink Mission",
                        rocket = Rocket("Falcon 9 Block 5"),
                        launchDateStatus = LaunchDateStatus.FUTURE,
                        launchDays = "5 days"
                    )
                )
            )
        } else {
            // Simulate a network error
            Result.Error(DataError.NETWORK_CONNECTION_FAILED)
        }
    }

    private fun shouldSimulateSuccess(isSuccess: Boolean): Boolean {
        return isSuccess
    }
}