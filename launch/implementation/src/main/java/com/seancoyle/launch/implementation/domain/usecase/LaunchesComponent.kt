package com.seancoyle.launch.implementation.domain.usecase

internal interface LaunchesComponent {
    fun getLaunchesFromNetworkAndInsertToCacheUseCase(): GetLaunchesFromNetworkAndInsertToCacheUseCase
    fun filterLaunchItemsInCacheUseCase(): FilterLaunchItemsInCacheUseCase
    fun createMergeLaunchesUseCase(): CreateMergedLaunchesUseCase
}