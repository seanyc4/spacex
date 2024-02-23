package com.seancoyle.launch.implementation.domain.usecase

import kotlinx.coroutines.flow.Flow

internal interface GetNumLaunchItemsFromCacheUseCase {
    operator fun invoke(): Flow<Int>
}