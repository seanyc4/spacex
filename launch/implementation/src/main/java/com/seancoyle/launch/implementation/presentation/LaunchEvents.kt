package com.seancoyle.launch.implementation.presentation

import com.seancoyle.core.domain.Response

internal sealed class LaunchEvents {
    data object GetLaunchesApiAndCacheEvent : LaunchEvents()

    data object GetCompanyApiAndCacheEvent : LaunchEvents()

    data object GetCompanyFromCacheEvent : LaunchEvents()

    data object PaginateLaunchesCacheEvent : LaunchEvents()

    data object GetNumLaunchesInCacheEvent : LaunchEvents()

    data object SortAndFilterLaunchesEvent : LaunchEvents()

    class CreateMessageEvent(
        val response: Response
    ) : LaunchEvents()

}