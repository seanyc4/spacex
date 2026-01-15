package com.seancoyle.feature.launch.domain.usecase.component

import androidx.paging.PagingData
import com.seancoyle.core.common.result.DataError
import com.seancoyle.core.common.result.LaunchResult
import com.seancoyle.core.domain.LaunchesType
import com.seancoyle.core.domain.Order
import com.seancoyle.feature.launch.domain.model.Launch
import com.seancoyle.feature.launch.domain.model.LaunchPrefs
import com.seancoyle.feature.launch.domain.model.LaunchSummary
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.GetLaunchesPreferencesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.ObservePastLaunchesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.ObserveUpcomingLaunchesUseCase
import com.seancoyle.feature.launch.domain.usecase.launch.SaveLaunchesPreferencesUseCase
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LaunchesComponentImpl @Inject constructor(
    private val saveLaunchesPreferencesUseCase: Lazy<SaveLaunchesPreferencesUseCase>,
    private val getLaunchesPreferencesUseCase: GetLaunchesPreferencesUseCase,
    private val observePastLaunchesUseCase: ObservePastLaunchesUseCase,
    private val observeUpcomingLaunchesUseCase: ObserveUpcomingLaunchesUseCase,
    private val getLaunchUseCase: GetLaunchUseCase
) : LaunchesComponent {

    override fun observeUpcomingLaunches(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>> {
        return observeUpcomingLaunchesUseCase(launchesQuery)
    }

    override fun observePastLaunches(launchesQuery: LaunchesQuery): Flow<PagingData<LaunchSummary>> {
        return observePastLaunchesUseCase.invoke(launchesQuery)
    }

    override suspend fun getLaunchPreferencesUseCase(): LaunchPrefs {
        return getLaunchesPreferencesUseCase.invoke()
    }

    override suspend fun saveLaunchPreferencesUseCase(order: Order) {
        saveLaunchesPreferencesUseCase.get().invoke(order)
    }

    override suspend fun getLaunchUseCase(
        launchId: String,
        launchType: LaunchesType
    ): LaunchResult<Launch, DataError.RemoteError> {
        return getLaunchUseCase.invoke(
            id = launchId,
            launchType = launchType
        )
    }
}
