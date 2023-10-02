package com.seancoyle.launch.implementation.presentation

import com.seancoyle.core.domain.StateMessage

sealed class LaunchEvents {

    data object GetLaunchesApiAndCacheEvent : LaunchEvents()

    data object GetCompanyInfoApiAndCacheEvent : LaunchEvents()

    data object GetCompanyInfoFromCacheEvent : LaunchEvents()

    data object FilterLaunchItemsInCacheEvent : LaunchEvents()

    data object GetNumLaunchItemsInCacheEvent : LaunchEvents()

    data object MergeDataEvent : LaunchEvents()

    class CreateMessageEvent(
        val stateMessage: StateMessage
    ) : LaunchEvents()

}