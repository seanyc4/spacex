package com.seancoyle.launch.implementation.domain.usecase

import com.seancoyle.core.data.cache.CacheResult
import com.seancoyle.core.data.network.safeCacheCall
import com.seancoyle.core.di.IODispatcher
import com.seancoyle.launch.api.domain.model.ViewType
import com.seancoyle.launch.implementation.data.cache.LaunchCacheDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FilterLaunchItemsInCacheUseCaseImpl @Inject constructor(
    private val cacheDataSource: LaunchCacheDataSource,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : FilterLaunchItemsInCacheUseCase {

    override suspend operator fun invoke(
        year: String?,
        order: String,
        launchFilter: Int?,
        page: Int?
    ): Flow<CacheResult<List<ViewType>?>> = flow {
        val result = safeCacheCall(ioDispatcher){
            cacheDataSource.filterLaunchList(
                year = year,
                order = order,
                launchFilter = launchFilter,
                page = page
            )
        }

        when(result){
            is CacheResult.Success -> {
                result.data?.let { launchList ->
                    emit(CacheResult.Success(launchList))
                }
            }
            is CacheResult.Error -> {
                emit(result)
            }
            else -> {}
        }
    }
}