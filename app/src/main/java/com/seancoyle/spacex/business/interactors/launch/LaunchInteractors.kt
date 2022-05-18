package com.seancoyle.spacex.business.interactors.launch

class LaunchInteractors(
    val getAllLaunchItemsFromCache: GetAllLaunchItemsFromCache,
    val getLaunchListFromNetworkAndInsertToCache: GetLaunchListFromNetworkAndInsertToCache,
    val getNumLaunchItemsFromCache: GetNumLaunchItemsFromCache,
    val filterLaunchItemsInCache: FilterLaunchItemsInCache
)














