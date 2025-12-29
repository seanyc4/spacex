package com.seancoyle.feature.launch.domain.model

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color
import com.seancoyle.core.domain.Order
import com.seancoyle.core.ui.designsystem.theme.AppColors

/**
 * If these classes are amended in any way then [com.seancoyle.core.datastore.proto] launch_prefs.proto
 * must be updated to ensure type safety in datastore-proto. Failure to update the proto file will
 * throw an exception at runtime.
 */
@Keep
data class LaunchPrefs(
    val order: Order = Order.ASC,
    val launchStatus: LaunchStatus = LaunchStatus.ALL,
    val query: String = ""
)

@Keep
enum class LaunchStatus(val text: String, val color: Color) {
    SUCCESS("Success", AppColors.Green),
    GO("Go", AppColors.Green),
    FAILED("Failed", AppColors.Red),
    TBC("TBC", AppColors.Amber),
    TBD("TBD", AppColors.Amber),
    ALL("All", AppColors.Black)
}
