package com.seancoyle.feature.launch.data.repository

import androidx.paging.Pager
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.feature.launch.di.PastLaunches
import com.seancoyle.feature.launch.di.UpcomingLaunches
import com.seancoyle.feature.launch.domain.model.LaunchesQuery
import javax.inject.Inject

internal class LaunchesPagerFactory @Inject constructor(
    @param:UpcomingLaunches private val upcoming: PagerFactory<UpcomingLaunchEntity>,
    @param:PastLaunches private val past: PagerFactory<PastLaunchEntity>,
) {

    fun createUpcoming(launchesQuery: LaunchesQuery): Pager<Int, UpcomingLaunchEntity> =
        upcoming.create(launchesQuery)

    fun createPast(launchesQuery: LaunchesQuery): Pager<Int, PastLaunchEntity> =
        past.create(launchesQuery)
}
