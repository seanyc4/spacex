package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.api.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase

class LaunchUseCases(
    val getLaunchesFromNetworkAndInsertToCacheUseCase: GetLaunchesFromNetworkAndInsertToCacheUseCase,
    val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
)














