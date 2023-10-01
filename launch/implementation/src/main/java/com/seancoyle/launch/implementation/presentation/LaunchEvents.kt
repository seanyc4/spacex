package com.seancoyle.launch.implementation.presentation

import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.StateMessage

sealed class LaunchEvents : Event {

    object FetchLaunchesAndCacheAndUpdateUiStateEvent : LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting launches from network."
        }
        override fun eventName(): String {
            return "GetLaunchesFromNetworkAndInsertToCacheEvent"
        }
        override fun shouldDisplayProgressBar() = true
    }

    object GetCompanyInfoFromNetworkAndInsertToCacheEvent : LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting company info from network."
        }
        override fun eventName(): String {
            return "GetCompanyInfoFromNetworkAndInsertToCacheEvent"
        }
        override fun shouldDisplayProgressBar() = true
    }

    object GetCompanyInfoFromCacheEvent: LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting company info from cache."
        }
        override fun eventName(): String {
            return "GetCompanyInfoFromCacheEvent"
        }
        override fun shouldDisplayProgressBar() = false
    }

    object FilterLaunchItemsInCacheEvent: LaunchEvents(){
        override fun errorInfo(): String {
            return "Error getting filtered launches from cache."
        }
        override fun eventName(): String {
            return "FilterLaunchItemsInCacheEvent"
        }
        override fun shouldDisplayProgressBar() = true
    }

    object GetNumLaunchItemsInCacheEvent : LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting the number of launches from the cache."
        }
        override fun eventName(): String {
            return "GetNumLaunchesInCacheEvent"
        }
        override fun shouldDisplayProgressBar() = false
    }

    object MergeDataEvent : LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting the number of launches from the cache."
        }
        override fun eventName(): String {
            return "MergeDataEvent"
        }
        override fun shouldDisplayProgressBar() = false
    }

    class CreateMessageEvent(
        val stateMessage: StateMessage
    ) : LaunchEvents() {
        override fun errorInfo(): String {
            return "Error creating a new state message."
        }
        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }
        override fun shouldDisplayProgressBar() = false
    }
}