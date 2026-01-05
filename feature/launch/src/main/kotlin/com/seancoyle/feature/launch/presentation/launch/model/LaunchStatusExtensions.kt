package com.seancoyle.feature.launch.presentation.launch.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BuildCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlagCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.seancoyle.core.ui.designsystem.theme.AppColors

@Stable
@Composable
fun LaunchStatus.containerColor(): Color = when (this) {
    LaunchStatus.SUCCESS -> Color(0xFF1B5E20)
    LaunchStatus.FAILED -> Color(0xFFB71C1C)
    LaunchStatus.TBD -> Color(0xFFF57F17)
    LaunchStatus.GO -> Color(0xFF1B5E20)
    LaunchStatus.TBC -> Color(0xFFF57F17)
    LaunchStatus.ALL -> Color(0xFF616161)
}

@Stable
@Composable
fun LaunchStatus.contentColor(): Color = when (this) {
    LaunchStatus.SUCCESS -> Color(0xFF4CAF50)
    LaunchStatus.FAILED -> Color(0xFFEF5350)
    LaunchStatus.TBD -> Color(0xFFFFEB3B)
    LaunchStatus.GO -> Color(0xFF4CAF50)
    LaunchStatus.TBC -> Color(0xFFFFEB3B)
    LaunchStatus.ALL -> Color(0xFF9E9E9E)
}

@Stable
fun LaunchStatus.pillColor(): Color = when (this) {
    LaunchStatus.SUCCESS -> AppColors.Green
    LaunchStatus.FAILED -> AppColors.Red
    LaunchStatus.TBD -> AppColors.Amber
    LaunchStatus.GO -> AppColors.Green
    LaunchStatus.TBC -> AppColors.Amber
    LaunchStatus.ALL -> AppColors.Black
}

@Stable
fun LaunchStatus.icon(): ImageVector = when (this) {
    LaunchStatus.SUCCESS -> Icons.Default.CheckCircle
    LaunchStatus.FAILED -> Icons.Default.BuildCircle
    LaunchStatus.TBD -> Icons.Default.FlagCircle
    LaunchStatus.GO -> Icons.Default.CheckCircle
    LaunchStatus.TBC -> Icons.Default.FlagCircle
    LaunchStatus.ALL -> Icons.Default.FlagCircle
}
