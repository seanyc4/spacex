package com.seancoyle.launch_usecases.launch

class LaunchUseCase(
    val getAllLaunchItemsFromCacheUseCase: GetAllLaunchItemsFromCacheUseCase,
    val getLaunchListFromNetworkAndInsertToCacheUseCase: GetLaunchListFromNetworkAndInsertToCacheUseCase,
    val getNumLaunchItemsFromCacheUseCase: GetNumLaunchItemsFromCacheUseCase,
    val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase
)














