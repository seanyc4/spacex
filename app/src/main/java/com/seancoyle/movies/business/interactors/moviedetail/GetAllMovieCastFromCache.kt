package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllMovieCastFromCache(
    private val cacheDataSource: MovieDetailCacheDataSource
) {

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<MovieDetailViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.getAll()
        }

        val response = object : CacheResponseHandler<MovieDetailViewState, List<MovieCast>?>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<MovieCast>?): DataState<MovieDetailViewState> {
                var message: String? =
                    GET_ALL_MOVIE_CASTS_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if (resultObj == null) {
                    message =
                        GET_ALL_MOVIE_CASTS_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
                        messageType = MessageType.Success
                    ),
                    data = MovieDetailViewState(
                        movieCastList = resultObj as ArrayList<MovieCast>?
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        val GET_ALL_MOVIE_CASTS_SUCCESS = "Successfully retrieved movie casts"
        val GET_ALL_MOVIE_CASTS_NO_MATCHING_RESULTS =
            "There are no movie casts that match that query."
        val GET_ALL_MOVIE_CAST_FAILED = "Failed to retrieve the movie casts."
        val NO_DATA = "Error getting movie casts from cache.\n\nReason: Cache data is null."

    }
}







