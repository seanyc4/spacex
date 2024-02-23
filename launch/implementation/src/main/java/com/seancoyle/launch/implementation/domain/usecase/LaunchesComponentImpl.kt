package com.seancoyle.launch.implementation.domain.usecase

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