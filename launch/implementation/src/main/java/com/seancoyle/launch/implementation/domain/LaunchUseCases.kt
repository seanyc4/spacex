package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.api.usecase.GetLaunchListFromNetworkAndInsertToCacheUseCase

class LaunchUseCases(
    val getLaunchListFromNetworkAndInsertToCacheUseCase: GetLaunchListFromNetworkAndInsertToCacheUseCase,
    val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
)














