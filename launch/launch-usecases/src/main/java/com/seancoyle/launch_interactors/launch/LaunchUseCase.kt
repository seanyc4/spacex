package com.seancoyle.launch_interactors.launch

class LaunchUseCase(
    val getAllLaunchItemsFromCacheUseCase: GetAllLaunchItemsFromCacheUseCase,
    val getLaunchListFromNetworkAndInsertToCacheUseCase: GetLaunchListFromNetworkAndInsertToCacheUseCase,
    val getNumLaunchItemsFromCacheUseCase: GetNumLaunchItemsFromCacheUseCase,
    val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
)














