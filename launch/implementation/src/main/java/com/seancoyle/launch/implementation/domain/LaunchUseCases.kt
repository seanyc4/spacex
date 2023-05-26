package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.contract.domain.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.contract.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase

class LaunchUseCases(
    val getLaunchesFromNetworkAndInsertToCacheUseCase: GetLaunchesFromNetworkAndInsertToCacheUseCase,
    val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
)














