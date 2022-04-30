package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieByIdFromCache(
    private val cacheDataSource: MovieListCacheDataSource
) {

    fun execute(
        id: String,
        stateEvent: StateEvent
    ): Flow<DataState<MovieListViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.getById(
                id = id
            )
        }

        val response = object : CacheResponseHandler<MovieListViewState, MoviesDomainEntity?>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: MoviesDomainEntity?): DataState<MovieListViewState> {
                var message: String? =
                    GET_MOVIE_BY_ID_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if (resultObj == null) {
                    message =
                        GET_MOVIE_BY_ID_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
                        messageType = MessageType.Success
                    ),
                    data = MovieListViewState(
                        movies = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        val GET_MOVIE_BY_ID_SUCCESS = "Successfully retrieved movie by id"
        val GET_MOVIE_BY_ID_NO_MATCHING_RESULTS = "There are no movies that match that query."
        val GET_MOVIE_BY_ID_FAILED = "Failed to retrieve the movie by id."
        val NO_DATA = "Error getting movie from cache.\n\nReason: Cache data is null."

    }
}







