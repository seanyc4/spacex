package com.seancoyle.navigation

import androidx.navigation3.runtime.NavKey
import com.seancoyle.core.domain.LaunchesType
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object Launches : Route

    @Serializable
    data object PlaceholderDetail : Route

    @Serializable
    data class Launch(
        val launchId: String,
        val launchesType: LaunchesType
    ) : Route

}
