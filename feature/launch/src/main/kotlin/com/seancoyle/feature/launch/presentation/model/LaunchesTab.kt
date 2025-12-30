package com.seancoyle.feature.launch.presentation.model

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.ui.graphics.vector.ImageVector
import com.seancoyle.feature.launch.R

data class LaunchesTab(
    @param:StringRes val title: Int,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector
) {

    companion object {
        fun provideTabs(): List<LaunchesTab> {
            return listOf(
                LaunchesTab(
                    title = R.string.upcoming_launches,
                    unselectedIcon = Icons.Outlined.RocketLaunch,
                    selectedIcon = Icons.Filled.RocketLaunch
                ),
                LaunchesTab(
                    title = R.string.past_launches,
                    unselectedIcon = Icons.Outlined.AccessTime,
                    selectedIcon = Icons.Filled.AccessTime
                )
            )
        }
    }
}
