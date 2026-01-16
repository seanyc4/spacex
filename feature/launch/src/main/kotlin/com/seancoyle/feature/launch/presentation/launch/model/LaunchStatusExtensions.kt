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

fun getLaunchStatusColors() = listOf(
    LaunchStatus.ALL to null,
    LaunchStatus.SUCCESS to AppColors.successDark,
    LaunchStatus.GO to AppColors.successDark,
    LaunchStatus.TBC to AppColors.warningDark,
    LaunchStatus.TBD to AppColors.warningDark,
    LaunchStatus.FAILED to AppColors.errorDark,
)

@Stable
@Composable
fun LaunchStatus.containerColor(): Color = when (this) {
    LaunchStatus.SUCCESS -> AppColors.successDark
    LaunchStatus.FAILED -> AppColors.errorDark
    LaunchStatus.TBD -> AppColors.warningDark
    LaunchStatus.GO -> AppColors.successDark
    LaunchStatus.TBC -> AppColors.warningDark
    LaunchStatus.ALL -> AppColors.neutralDark
}

@Stable
@Composable
fun LaunchStatus.contentColor(): Color = when (this) {
    LaunchStatus.SUCCESS -> AppColors.success
    LaunchStatus.FAILED -> AppColors.errorContainer
    LaunchStatus.TBD -> AppColors.warningContainer
    LaunchStatus.GO -> AppColors.success
    LaunchStatus.TBC -> AppColors.warningContainer
    LaunchStatus.ALL -> AppColors.neutral
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
