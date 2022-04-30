package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.network.ApiResponseHandler
import com.seancoyle.movies.business.data.network.abstraction.movielist.MovieListNetworkDataSource
import com.seancoyle.movies.business.data.util.safeApiCall
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMoviesFromNetwork
constructor(
    private val movieListNetworkDataSource: MovieListNetworkDataSource
) {

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<MovieListViewState>?> = flow {

        val networkResult = safeApiCall(Dispatchers.IO) {
            movieListNetworkDataSource.get()
        }

        val networkResponse = object : ApiResponseHandler<MovieListViewState, MoviesDomainEntity?>(
            response = networkResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: MoviesDomainEntity?): DataState<MovieListViewState> {
                return if (resultObj != null) {
                    val viewState =
                        MovieListViewState(
                            movies = resultObj
                        )
                    DataState.data(
                        response = Response(
                            message = NETWORK_SUCCESS,
                            uiComponentType = UIComponentType.None,
                            messageType = MessageType.Success
                        ),
                        data = viewState,
                        stateEvent = stateEvent
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
    }

    companion object {
        val NETWORK_SUCCESS = "Successfully retrieved movies from network."
        val NETWORK_EMPTY = "No data returned from network."
        val ERROR = "Error updating movies from network.\n\nReason: Network error"
    }
}