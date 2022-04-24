package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.data.network.ApiResponseHandler
import com.seancoyle.movies.business.data.network.abstraction.movielist.MovieListNetworkDataSource
import com.seancoyle.movies.business.data.util.safeApiCall
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMoviesFromNetworkAndInsertToCache
constructor(
    private val cacheDataSource: MovieListCacheDataSource,
    private val movieListNetworkDataSource: MovieListNetworkDataSource,
    private val factory: MovieListFactory
) {

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<MovieListViewState>?> = flow {

        val networkResult = safeApiCall(Dispatchers.IO) {
            movieListNetworkDataSource.get()
        }

        val networkResponse = object : ApiResponseHandler<MovieListViewState, MovieParent?>(
            response = networkResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: MovieParent?): DataState<MovieListViewState> {
                return if (resultObj != null) {
                    val viewState =
                        MovieListViewState(
                            movieParent = resultObj
                        )
                    DataState.data(
                        response = null,
                        data = viewState,
                        stateEvent = null
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = NETWORK_EMPTY,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }

            override suspend fun handleFailure(): DataState<MovieListViewState> {
                return DataState.error(
                    response = Response(
                        message = ERROR,
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()
        emit(networkResponse)

        // Insert to Cache
        if (networkResponse?.data != null) {

            val movies = factory.createSingleMovie(
                id = "1",
                category = "Marvel",
                page = networkResponse.data?.movieParent?.page,
                movies = networkResponse.data?.movieParent?.movies,
                total_results = networkResponse.data?.movieParent?.total_results,
                total_pages = networkResponse.data?.movieParent?.total_pages
            )

            val cacheResult = safeCacheCall(Dispatchers.IO) {
                cacheDataSource.insert(movies)
            }

            val cacheResponse = object : CacheResponseHandler<MovieListViewState, Long>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Long): DataState<MovieListViewState> {
                    return if (resultObj > 0) {
                        DataState.data(
                            response = Response(
                                message = INSERT_SUCCESS,
                                uiComponentType = UIComponentType.None,
                                messageType = MessageType.Success
                            ),
                            data = null,
                            stateEvent = stateEvent
                        )
                    } else {
                        DataState.data(
                            response = Response(
                                message = INSERT_FAILED,
                                uiComponentType = UIComponentType.None,
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
    }

    companion object {
        val NETWORK_SUCCESS = "Successfully retrieved movies from network."
        val NETWORK_EMPTY = "No data returned from network."
        val ERROR = "Error updating movies from network.\n\nReason: Network error"
        val INSERT_SUCCESS = "Successfully inserted movies from network."
        val INSERT_FAILED = "Failed to insert movies from network."
    }
}