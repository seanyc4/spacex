package com.seancoyle.launch.implementation.presentation

import com.seancoyle.core.state.Event
import com.seancoyle.core.state.StateMessage

sealed class LaunchEvent : Event {

    object GetLaunchListFromNetworkAndInsertToCacheEvent : LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting launch list from network."
        }

        override fun eventName(): String {
            return "GetLaunchItemsFromNetworkAndInsertToCacheEvent"
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
            return "Error getting list of launch items."
        }

        override fun eventName(): String {
            return "FilterLaunchItemsInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetNumLaunchItemsInCacheEvent : LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting the number of launch items from the cache."
        }

        override fun eventName(): String {
            return "GetNumLaunchItemsInCacheEvent"
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




















