package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.api.usecase.GetAllLaunchItemsFromCacheUseCase
import com.seancoyle.launch.api.usecase.GetLaunchListFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.api.usecase.GetNumLaunchItemsFromCacheUseCase

class LaunchUseCases(
    val getAllLaunchItemsFromCacheUseCase: GetAllLaunchItemsFromCacheUseCase,
    val getLaunchListFromNetworkAndInsertToCacheUseCase: GetLaunchListFromNetworkAndInsertToCacheUseCase,
    val getNumLaunchItemsFromCacheUseCase: GetNumLaunchItemsFromCacheUseCase,
    val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
)














