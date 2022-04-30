package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMovieCastByIdFromCache(
    private val cacheDataSource: MovieDetailCacheDataSource
) {

    fun execute(
        id: Int,
        stateEvent: StateEvent
    ): Flow<DataState<MovieDetailViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO) {
            cacheDataSource.getById(
                id = id
            )
        }

        val response = object : CacheResponseHandler<MovieDetailViewState, MovieCastDomainEntity?>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: MovieCastDomainEntity?): DataState<MovieDetailViewState> {
                var message: String? =
                    GET_MOVIE_CAST_BY_ID_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if (resultObj == null) {
                    message =
                        GET_MOVIE_CAST_BY_ID_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
                        messageType = MessageType.Success
                    ),
                    data = MovieDetailViewState(
                        movieCast = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        val GET_MOVIE_CAST_BY_ID_SUCCESS = "Successfully retrieved movie cast"
        val GET_MOVIE_CAST_BY_ID_NO_MATCHING_RESULTS = "There are no movie cast that match that query."
        val GET_MOVIE_CAST_BY_ID_FAILED = "Failed to retrieve the movie cast."
        val NO_DATA = "Error getting movie cast from cache.\n\nReason: Cache data is null."

    }
}







