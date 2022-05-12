package com.seancoyle.spacex.framework.presentation.launch.state

import com.seancoyle.spacex.business.domain.model.company.CompanyInfoDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.state.StateEvent
import com.seancoyle.spacex.business.domain.state.StateMessage

sealed class LaunchStateEvent : StateEvent {

    object GetLaunchListFromNetworkAndInsertToCacheEvent : LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error getting launch from network."
        }

        override fun eventName(): String {
            return "GetLaunchListFromNetworkAndInsertToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetCompanyInfoFromNetworkAndInsertToCacheEvent : LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error getting company info from network."
        }

        override fun eventName(): String {
            return "GetCompanyInfoFromNetworkAndInsertToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetLaunchListFromCacheEvent: LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error getting launch list from cache."
        }

        override fun eventName(): String {
            return "GetLaunchListFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetLaunchItemFromCacheEvent
        constructor(
            private val id: Int
        ): LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error getting launch item from cache."
        }

        override fun eventName(): String {
            return "GetLaunchItemFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetCompanyInfoFromCacheEvent: LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error getting company info from cache."
        }

        override fun eventName(): String {
            return "GetCompanyInfoFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetNumLaunchItemsInCacheEvent : LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error getting the number of launch items from the cache."
        }

        override fun eventName(): String {
            return "GetNumLaunchItemsInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class InsertCompanyInfoToCacheEvent
        constructor(
            private val companyInfo: CompanyInfoDomainEntity
        ): LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting company info."
        }

        override fun eventName(): String {
            return "InsertCompanyInfoToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class InsertLaunchListToCacheEvent
    constructor(
        private val launchList: List<LaunchDomainEntity>
    ): LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting launch list."
        }

        override fun eventName(): String {
            return "InsertLaunchListToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ) : LaunchStateEvent() {

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















