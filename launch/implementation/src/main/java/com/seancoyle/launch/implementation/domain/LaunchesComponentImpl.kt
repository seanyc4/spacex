package com.seancoyle.launch.implementation.domain

import com.seancoyle.launch.api.domain.usecase.CreateMergedLaunchesUseCase
import com.seancoyle.launch.api.domain.usecase.FilterLaunchItemsInCacheUseCase
import com.seancoyle.launch.api.domain.usecase.GetLaunchesFromNetworkAndInsertToCacheUseCase
import com.seancoyle.launch.api.domain.usecase.LaunchesComponent
import javax.inject.Inject

internal class LaunchesComponentImpl @Inject constructor(
    private val getLaunchesFromNetworkAndInsertToCacheUseCase: GetLaunchesFromNetworkAndInsertToCacheUseCase,
    private val filterLaunchItemsInCacheUseCase: FilterLaunchItemsInCacheUseCase,
    private val mergedLaunchesUseCase: CreateMergedLaunchesUseCase
): LaunchesComponent {

    override fun getLaunchesFromNetworkAndInsertToCacheUseCase(): GetLaunchesFromNetworkAndInsertToCacheUseCase {
        return getLaunchesFromNetworkAndInsertToCacheUseCase
    }

    override fun filterLaunchItemsInCacheUseCase(): FilterLaunchItemsInCacheUseCase {
       return filterLaunchItemsInCacheUseCase
    }

    override fun createMergeLaunchesUseCase(): CreateMergedLaunchesUseCase {
        return mergedLaunchesUseCase
    }
}