package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.domain.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.api.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase

class LaunchUseCases(
    val getLaunchesFromNetworkAndInsertToCacheUseCase: GetLaunchesFromNetworkAndInsertToCacheUseCase,
    val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
)