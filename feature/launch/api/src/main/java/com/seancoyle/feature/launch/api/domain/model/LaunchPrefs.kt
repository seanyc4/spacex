package com.seancoyle.feature.launch.api.domain.model

import com.google.errorprone.annotations.Keep
import com.seancoyle.core.domain.Order

/**
 * If these classes are amended in any way then [com.seancoyle.core.datastore.proto] launch_prefs.proto
 * must be updated to ensure type safety in datastore-proto. Failure to update the proto file will
 * throw an exception at runtime.
 */
@Keep
data class LaunchPrefs(
    val order: Order,
    val launchStatus: LaunchStatus,
    val launchYear: String
)

@Keep
enum class LaunchStatus {
    SUCCESS, FAILED, UNKNOWN, ALL
}

@Keep
enum class LaunchDateStatus {
    PAST, FUTURE
}