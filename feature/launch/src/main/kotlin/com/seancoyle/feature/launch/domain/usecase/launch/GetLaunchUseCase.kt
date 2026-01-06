package com.seancoyle.feature.launch.domain.usecase.launch

import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.repository.LaunchesRepository
import javax.inject.Inject

internal class GetLaunchUseCase @Inject constructor(
    private val launchesRepository: LaunchesRepository
) {
    suspend operator fun invoke(
        id: String,
        launchType: LaunchesType
    ): LaunchResult<Launch, DataError.RemoteError> {
        return when (val result = launchesRepository.getLaunch(id, launchType)) {
            is LaunchResult.Success -> {
                val filteredResult = filterYouTubeVideos(result.data)
                LaunchResult.Success(filteredResult)
            }

            is LaunchResult.Error -> result
        }
    }

    private fun filterYouTubeVideos(launch: Launch): Launch {
        val filteredVidUrls = launch.vidUrls.filter {
            it.source?.contains("youtube", ignoreCase = true) == true
        }
        val filteredLaunch = launch.copy(vidUrls = filteredVidUrls)
        return filteredLaunch
    }
}
