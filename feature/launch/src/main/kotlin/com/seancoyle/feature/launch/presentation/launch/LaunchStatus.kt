package com.seancoyle.feature.launch.presentation.launch

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlagCircle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.seancoyle.core.ui.designsystem.theme.AppColors

enum class LaunchStatus(
    val label: String,
    val abbrev: String,
    val containerColor: Color,
    val contentColor: Color,
    val pillColor: Color,
    val icon: ImageVector
) {
    SUCCESS(
        label = "Success",
        abbrev = "Success",
        containerColor = Color(0xFF1B5E20),
        contentColor = Color(0xFF4CAF50),
        pillColor = AppColors.Green,
        icon = Icons.Default.CheckCircle
    ),
    FAILED(
        label = "Failed",
        abbrev = "Failed",
        containerColor = Color(0xFFB71C1C),
        contentColor = Color(0xFFEF5350),
        pillColor = AppColors.Red,
        icon = Icons.Default.BuildCircle
    ),
    TBD(
        label = "To be Determined",
        abbrev = "TBD",
        containerColor = Color(0xFFF57F17),
        contentColor = Color(0xFFFFEB3B),
        pillColor = AppColors.Amber,
        icon = Icons.Default.FlagCircle

    ),
    GO(
        label = "Go for Launch",
        abbrev = "Go",
        containerColor = Color(0xFF1B5E20),
        contentColor = Color(0xFF4CAF50),
        pillColor = AppColors.Green,
        icon = Icons.Default.CheckCircle
    ),
    TBC(
        label = "To Be Confirmed",
        abbrev = "TBC",
        containerColor = Color(0xFFF57F17),
        contentColor = Color(0xFFFFEB3B),
        pillColor = AppColors.Amber,
        icon = Icons.Default.FlagCircle
    ),
    ALL(
        label = "Unknown",
        abbrev = "All",
        containerColor = Color(0xFF616161),
        contentColor = Color(0xFF9E9E9E),
        pillColor = AppColors.Black,
        icon = Icons.Default.FlagCircle
    )
}
