package com.seancoyle.launch.implementation.presentation

import com.seancoyle.core.domain.Event
import com.seancoyle.core.domain.StateMessage

sealed class LaunchEvents : Event {

    object GetLaunchesFromNetworkAndInsertToCacheEvents : LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting launches from network."
        }
        override fun eventName(): String {
            return "GetLaunchesFromNetworkAndInsertToCacheEvent"
        }
        override fun shouldDisplayProgressBar() = true
    }

    object GetCompanyInfoFromNetworkAndInsertToCacheEvents : LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting company info from network."
        }
        override fun eventName(): String {
            return "GetCompanyInfoFromNetworkAndInsertToCacheEvent"
        }
        override fun shouldDisplayProgressBar() = true
    }

    object GetCompanyInfoFromCacheEvents: LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting company info from cache."
        }
        override fun eventName(): String {
            return "GetCompanyInfoFromCacheEvent"
        }
        override fun shouldDisplayProgressBar() = false
    }

    object FilterLaunchItemsInCacheEvents: LaunchEvents(){
        override fun errorInfo(): String {
            return "Error getting filtered launches from cache."
        }
        override fun eventName(): String {
            return "FilterLaunchItemsInCacheEvent"
        }
        override fun shouldDisplayProgressBar() = true
    }

    object GetNumLaunchItemsInCacheEvents : LaunchEvents() {
        override fun errorInfo(): String {
            return "Error getting the number of launches from the cache."
        }
        override fun eventName(): String {
            return "GetNumLaunchesInCacheEvent"
        }
        override fun shouldDisplayProgressBar() = false
    }

    class CreateMessageEvents(
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