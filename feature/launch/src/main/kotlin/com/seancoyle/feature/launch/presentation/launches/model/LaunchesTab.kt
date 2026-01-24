package com.seancoyle.feature.launch.presentation.launches.model

import androidx.annotation.StringRes
import com.seancoyle.feature.launch.R

data class LaunchesTab(
    @param:StringRes val title: Int
) {

    companion object {
        fun provideTabs(): List<LaunchesTab> {
            return listOf(
                LaunchesTab(
                    title = R.string.upcoming_launches
                ),
                LaunchesTab(
                    title = R.string.past_launches
                )
            )
        }
    }
}
