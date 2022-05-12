package com.seancoyle.spacex.business.interactors.launch

import com.seancoyle.spacex.business.data.cache.CacheResponseHandler
import com.seancoyle.spacex.business.domain.state.*
import com.seancoyle.spacex.business.data.util.safeCacheCall
import com.seancoyle.spacex.business.data.cache.abstraction.launch.LaunchCacheDataSource
import com.seancoyle.spacex.business.domain.model.launch.LaunchDomainEntity
import com.seancoyle.spacex.business.domain.model.launch.LaunchFactory
import com.seancoyle.spacex.framework.presentation.launch.state.LaunchViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertLaunchListToCache(
    private val cacheDataSource: LaunchCacheDataSource,
    private val factory: LaunchFactory
) {

    fun execute(
        launchList: List<LaunchDomainEntity>,
        stateEvent: StateEvent
    ): Flow<DataState<LaunchViewState>?> = flow {

        val newLaunchList = factory.createLaunchList(
            launchList
        )

        val cacheResult = safeCacheCall(IO) {
            cacheDataSource.insertLaunchList(newLaunchList)
        }

        val cacheResponse = object : CacheResponseHandler<LaunchViewState, LongArray>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: LongArray): DataState<LaunchViewState> {
                return if (resultObj.isNotEmpty()) {
                    val viewState =
                        LaunchViewState(
                            launchList = newLaunchList
                        )
                    DataState.data(
                        response = Response(
                            message = INSERT_LAUNCH_LIST_SUCCESS,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Success
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = INSERT_LAUNCH_LIST_FAILED,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(cacheResponse)
    }

    companion object {
        const val INSERT_LAUNCH_LIST_SUCCESS = "Successfully inserted new launch list."
        const val INSERT_LAUNCH_LIST_FAILED = "Failed to insert new launch list."
    }
}