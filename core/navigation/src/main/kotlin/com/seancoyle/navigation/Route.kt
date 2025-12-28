package com.seancoyle.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object LaunchList : Route, NavKey

   /* @Serializable
    data class LaunchDetails(val launchId: String) : Route, NavKey*/

}
