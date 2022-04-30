package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteMovieCast<ViewState>(
    private val cacheDataSource: MovieDetailCacheDataSource
) {

    fun execute(
        movieCast: MovieCastDomainEntity,
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>?> = flow {

        val cacheResult = safeCacheCall(IO) {
            cacheDataSource.deleteById(movieCast.id)
        }

        val response = object : CacheResponseHandler<ViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Int): DataState<ViewState>? {
                return if (resultObj > 0) {
                    DataState.data(
                        response = Response(
                            message = DELETE_MOVIE_CAST_SUCCESS,
                            uiComponentType = UIComponentType.None,
                            messageType = MessageType.Success
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = DELETE_MOVIE_CAST_FAILED,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(response)
    }

    companion object {
        val DELETE_MOVIE_CAST_SUCCESS = "Successfully deleted movie cast."
        val DELETE_MOVIE_CAST_FAILED = "Failed to delete movie cast."
    }
}













