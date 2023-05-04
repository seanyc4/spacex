package com.seancoyle.launch.api.usecase

import com.seancoyle.core.state.DataState
import com.seancoyle.core.state.Event
import com.seancoyle.launch.api.model.LaunchModel
import com.seancoyle.launch.api.model.LaunchViewState
import kotlinx.coroutines.flow.Flow

interface InsertLaunchListToCacheUseCase {
    operator fun invoke(
        launchList: List<LaunchModel>,
        event: Event
    ): Flow<DataState<LaunchViewState>?>
}