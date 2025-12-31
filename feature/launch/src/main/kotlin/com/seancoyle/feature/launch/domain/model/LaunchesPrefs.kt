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
enum class LaunchStatus(
    val label: String,
    val abbrev: String,
    val containerColor: Color,
    val contentColor: Color,
    val pillColor: Color
) {
    SUCCESS(
        label = "Success",
        abbrev = "Success",
        containerColor = Color(0xFF1B5E20),
        contentColor = Color(0xFF4CAF50),
        pillColor = AppColors.Green
    ),
    FAILED(
        label = "Failed",
        abbrev = "Failed",
        containerColor = Color(0xFFB71C1C),
        contentColor = Color(0xFFEF5350),
        pillColor = AppColors.Red
    ),
    TBD(
        label = "To be Determined",
        abbrev = "TBD",
        containerColor = Color(0xFFF57F17),
        contentColor = Color(0xFFFFEB3B),
        pillColor = AppColors.Amber
    ),
    GO(
        label = "Go for Launch",
        abbrev = "Go",
        containerColor = Color(0xFF1B5E20),
        contentColor = Color(0xFF4CAF50),
        pillColor = AppColors.Green
    ),
    TBC(
        label = "To Be Confirmed",
        abbrev = "TBC",
        containerColor = Color(0xFFF57F17),
        contentColor = Color(0xFFFFEB3B),
        pillColor = AppColors.Amber
    ),
    ALL(
        label = "Unknown",
        abbrev = "All",
        containerColor = Color(0xFF616161),
        contentColor = Color(0xFF9E9E9E),
        pillColor = AppColors.Black
    )
}
