package com.seancoyle.launch.api.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetNumLaunchItemsFromCacheUseCase {
    operator fun invoke(): Flow<Int>
}