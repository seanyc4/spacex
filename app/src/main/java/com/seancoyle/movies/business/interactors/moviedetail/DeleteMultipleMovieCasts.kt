package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteMultipleMovieCasts(
    private val cacheDataSource: MovieDetailCacheDataSource
) {

    // set true if an error occurs when deleting any of the movie casts from cache
    private var onDeleteError: Boolean = false


    fun execute(
        movieCastList: List<MovieCastDomainEntity>,
        stateEvent: StateEvent
    ): Flow<DataState<MovieDetailViewState>?> = flow {

        val successfulDeletes: ArrayList<MovieCastDomainEntity> =
            ArrayList() // movie casts that were successfully deleted
        for (cast in movieCastList) {
            val cacheResult = safeCacheCall(IO) {
                cacheDataSource.deleteById(cast.id)
            }

            val response = object : CacheResponseHandler<MovieDetailViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Int): DataState<MovieDetailViewState>? {
                    if (resultObj < 0) { // if error
                        onDeleteError = true
                    } else {
                        successfulDeletes.add(cast)
                    }
                    return null
                }
            }.getResult()

            // check for random errors
            if (response?.stateMessage?.response?.message
                    ?.contains(stateEvent.errorInfo()) == true
            ) {
                onDeleteError = true
            }

        }

        if (onDeleteError) {
            emit(
                DataState.data(
                    response = Response(
                        message = DELETE_MOVIE_CASTS_ERRORS,
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Success
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        } else {
            emit(
                DataState.data(
                    response = Response(
                        message = DELETE_MOVIE_CASTS_SUCCESS,
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Success
                    ),
                    data = null,
                    stateEvent = stateEvent
                )
            )
        }

    }

    companion object {
        val DELETE_MOVIE_CASTS_SUCCESS = "Successfully deleted movie casts."
        val DELETE_MOVIE_CASTS_ERRORS =
            "Not all the movie casts you selected were deleted. There was some errors."
        val DELETE_MOVIE_CASTS_YOU_MUST_SELECT = "You haven't selected any movie casts to delete."
        val DELETE_MOVIE_CASTS_ARE_YOU_SURE = "Are you sure you want to delete these?"
    }

}













