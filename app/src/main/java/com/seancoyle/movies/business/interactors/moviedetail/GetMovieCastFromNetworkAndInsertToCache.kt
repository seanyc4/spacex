package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.data.network.ApiResponseHandler
import com.seancoyle.movies.business.data.network.abstraction.moviedetail.MovieDetailNetworkDataSource
import com.seancoyle.movies.business.data.util.safeApiCall
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieCastFromNetworkAndInsertToCache
constructor(
    private val cacheDataSource: MovieDetailCacheDataSource,
    private val networkDataSource: MovieDetailNetworkDataSource,
    private val factory: MovieDetailFactory
) {

    fun execute(
        movieId: String,
        stateEvent: StateEvent
    ): Flow<DataState<MovieDetailViewState>?> = flow {

        val networkResult = safeApiCall(Dispatchers.IO) {
            networkDataSource.getCast(movieId = movieId)
        }

        val networkResponse = object : ApiResponseHandler<MovieDetailViewState, MovieCastDomainEntity?>(
            response = networkResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: MovieCastDomainEntity?): DataState<MovieDetailViewState> {
                return if (resultObj != null) {
                    val viewState =
                        MovieDetailViewState(
                            movieCast = resultObj
                        )
                    DataState.data(
                        response = null,
                        data = viewState,
                        stateEvent = null
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = MOVIE_CAST_NETWORK_EMPTY,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }

            override suspend fun handleFailure(): DataState<MovieDetailViewState> {
                return DataState.error(
                    response = Response(
                        message = MOVIE_CAST_ERROR,
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        if (networkResponse?.stateMessage?.response?.message?.equals(MOVIE_CAST_ERROR) == true) {
            emit(networkResponse)
        }

        // Insert to Cache
        if (networkResponse?.data != null) {

            val movieCast = factory.createSingleMovieCast(
                id = networkResponse.data?.movieCast?.id,
                crewList = networkResponse.data?.movieCast?.crew!!,
                castList = networkResponse.data?.movieCast?.cast!!,
            )

            val cacheResult = safeCacheCall(Dispatchers.IO) {
                cacheDataSource.insert(movieCast)
            }

            val cacheResponse = object : CacheResponseHandler<MovieDetailViewState, Long>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Long): DataState<MovieDetailViewState> {
                    return if (resultObj > 0) {
                        val viewState =
                            MovieDetailViewState(
                                movieCast = movieCast
                            )
                        DataState.data(
                            response = Response(
                                message = MOVIE_CAST_INSERT_SUCCESS,
                                uiComponentType = UIComponentType.None,
                                messageType = MessageType.Success
                            ),
                            data = viewState,
                            stateEvent = stateEvent
                        )
                    } else {
                        DataState.data(
                            response = Response(
                                message = MOVIE_CAST_INSERT_FAILED,
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
        val MOVIE_CAST_NETWORK_SUCCESS = "Successfully retrieved movie cast from network."
        val MOVIE_CAST_NETWORK_EMPTY = "No data returned from network."
        val MOVIE_CAST_ERROR = "Error updating movie cast from network.\n\nReason: Network error"
        val MOVIE_CAST_INSERT_SUCCESS = "Successfully inserted movie cast from network."
        val MOVIE_CAST_INSERT_FAILED = "Failed to insert movie cast from network."
    }
}