package com.seancoyle.launch.implementation.presentation

import com.seancoyle.core.state.Event
import com.seancoyle.core.state.StateMessage

sealed class LaunchEvent : Event {

    object GetLaunchesFromNetworkAndInsertToCacheEvent : LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting launches from network."
        }

        override fun eventName(): String {
            return "GetLaunchesFromNetworkAndInsertToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetCompanyInfoFromNetworkAndInsertToCacheEvent : LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting company info from network."
        }

        override fun eventName(): String {
            return "GetCompanyInfoFromNetworkAndInsertToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetCompanyInfoFromCacheEvent: LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting company info from cache."
        }

        override fun eventName(): String {
            return "GetCompanyInfoFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

    object FilterLaunchItemsInCacheEvent: LaunchEvent(){

        override fun errorInfo(): String {
            return "Error getting filtered launches from cache."
        }

        override fun eventName(): String {
            return "FilterLaunchItemsInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetNumLaunchItemsInCacheEvent : LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting the number of launches from the cache."
        }

        override fun eventName(): String {
            return "GetNumLaunchesInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class CreateMessageEvent(
        val stateMessage: StateMessage
    ) : LaunchEvent() {

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















