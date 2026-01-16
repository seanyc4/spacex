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
    LaunchStatus.SUCCESS to AppColors.success,
    LaunchStatus.GO to AppColors.info,
    LaunchStatus.TBC to AppColors.tbc,
    LaunchStatus.TBD to AppColors.tbd,
    LaunchStatus.FAILED to AppColors.error,
)

@Stable
@Composable
fun LaunchStatus.containerColor(): Color = when (this) {
    LaunchStatus.SUCCESS -> AppColors.successDark
    LaunchStatus.FAILED -> AppColors.errorDark
    LaunchStatus.TBD -> AppColors.tbdDark
    LaunchStatus.GO -> AppColors.infoDark
    LaunchStatus.TBC -> AppColors.tbcDark
    LaunchStatus.ALL -> AppColors.neutralDark
}

@Stable
@Composable
fun LaunchStatus.contentColor(): Color = when (this) {
    LaunchStatus.SUCCESS -> AppColors.successContainer
    LaunchStatus.FAILED -> AppColors.errorContainer
    LaunchStatus.TBD -> AppColors.tbdContainer
    LaunchStatus.GO -> AppColors.infoContainer
    LaunchStatus.TBC -> AppColors.tbcContainer
    LaunchStatus.ALL -> AppColors.neutral
}

@Stable
fun LaunchStatus.pillColor(): Color = when (this) {
    LaunchStatus.SUCCESS -> AppColors.Green
    LaunchStatus.FAILED -> AppColors.Red
    LaunchStatus.TBD -> AppColors.tbd
    LaunchStatus.GO -> AppColors.info
    LaunchStatus.TBC -> AppColors.tbc
    LaunchStatus.ALL -> AppColors.neutralDark
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
