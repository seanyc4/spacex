package com.seancoyle.launch.implementation.presentation

import com.seancoyle.core.state.Event
import com.seancoyle.core.state.StateMessage
import com.seancoyle.launch.api.model.CompanyInfoModel
import com.seancoyle.launch.api.model.LaunchModel

sealed class LaunchEvent : Event {

    object GetLaunchListFromNetworkAndInsertToCacheEvent : LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting launch from network."
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

    object GetAllLaunchItemsFromCacheEvent: LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting launch list from cache."
        }

        override fun eventName(): String {
            return "GetAllLaunchItemsFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class GetLaunchItemFromCacheEvent
        constructor(
            private val id: Int
        ): LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting launch item from cache."
        }

        override fun eventName(): String {
            return "GetLaunchItemFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = false
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

    object GetNumLaunchItemsInCacheEvent : LaunchEvent() {

        override fun errorInfo(): String {
            return "Error getting the number of launch items from the cache."
        }

        override fun eventName(): String {
            return "GetNumLaunchItemsInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class InsertCompanyInfoToCacheEvent
        constructor(
            private val companyInfo: CompanyInfoModel
        ): LaunchEvent() {

        override fun errorInfo(): String {
            return "Error inserting company info."
        }

        override fun eventName(): String {
            return "InsertCompanyInfoToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class InsertLaunchItemsToCacheEvent
    constructor(
        private val launchList: List<LaunchModel>
    ): LaunchEvent() {

        override fun errorInfo(): String {
            return "Error inserting launch list."
        }

        override fun eventName(): String {
            return "InsertLaunchItemsToCacheEvent"
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




















