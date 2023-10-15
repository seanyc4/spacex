package com.seancoyle.launch.api.domain.usecase

interface LaunchesComponent {
    fun getLaunchesFromNetworkAndInsertToCacheUseCase(): GetLaunchesFromNetworkAndInsertToCacheUseCase
    fun filterLaunchItemsInCacheUseCase(): FilterLaunchItemsInCacheUseCase
    fun createMergeLaunchesUseCase(): CreateMergedLaunchesUseCase
}