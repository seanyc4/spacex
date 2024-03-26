package com.seancoyle.launch.implementation.presentation

import com.seancoyle.core.domain.Response
internal sealed class LaunchEvents {

    data object GetLaunchesApiAndCacheEvent : LaunchEvents()

    data object GetCompanyInfoApiAndCacheEvent : LaunchEvents()

    data object GetCompanyInfoFromCacheEvent : LaunchEvents()

    data object PaginateLaunchesCacheEvent : LaunchEvents()

    data object GetNumLaunchItemsInCacheEvent : LaunchEvents()

    data object CreateMergedAndFilteredLaunchesEvent : LaunchEvents()

    class CreateMessageEvent(
        val response: Response
    ) : LaunchEvents()

}