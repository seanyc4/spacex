package com.seancoyle.spacex.business.interactors.launch

class LaunchInteractors(
    val getAllLaunchItemsFromCache: GetAllLaunchItemsFromCache,
    val getLaunchItemByIdFromCache: GetLaunchItemByIdFromCache,
    val getLaunchItemsFromNetworkAndInsertToCache: GetLaunchItemsFromNetworkAndInsertToCache,
    val getNumLaunchItemsFromCache: GetNumLaunchItemsFromCache,
    val insertLaunchListToCache: InsertLaunchListToCache,
    val searchLaunchItemsInCache: SearchLaunchItemsInCache
)














