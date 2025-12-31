package com.seancoyle.feature.launch.domain.model

import androidx.annotation.Keep
import com.seancoyle.core.domain.Order

/**
 * If these classes are amended in any way then [com.seancoyle.core.datastore.proto] launch_prefs.proto
 * must be updated to ensure type safety in datastore-proto. Failure to update the proto file will
 * throw an exception at runtime.
 */
@Keep
data class LaunchPrefs(
    val order: Order = Order.ASC,
)
