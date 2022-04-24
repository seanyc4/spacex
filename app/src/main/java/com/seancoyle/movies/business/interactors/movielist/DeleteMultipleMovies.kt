package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteMultipleMovies(
    private val cacheDataSource: MovieListCacheDataSource
) {

    // set true if an error occurs when deleting any of the movies from cache
    private var onDeleteError: Boolean = false


    fun execute(
        movies: List<MovieParent>,
        stateEvent: StateEvent
    ): Flow<DataState<MovieListViewState>?> = flow {

        val successfulDeletes: ArrayList<MovieParent> =
            ArrayList() // movies that were successfully deleted
        for (movie in movies) {
            val cacheResult = safeCacheCall(IO) {
                cacheDataSource.deleteById(movie.id)
            }

            val response = object : CacheResponseHandler<MovieListViewState, Int>(
                response = cacheResult,
                stateEvent = stateEvent
            ) {
                override suspend fun handleSuccess(resultObj: Int): DataState<MovieListViewState>? {
                    if (resultObj < 0) { // if error
                        onDeleteError = true
                    } else {
                        successfulDeletes.add(movie)
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
                        message = DELETE_MOVIES_ERRORS,
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
                        message = DELETE_MOVIES_SUCCESS,
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
        val DELETE_MOVIES_SUCCESS = "Successfully deleted movies."
        val DELETE_MOVIES_ERRORS =
            "Not all the movies you selected were deleted. There was some errors."
        val DELETE_MOVIES_YOU_MUST_SELECT = "You haven't selected any movies to delete."
        val DELETE_MOVIES_ARE_YOU_SURE = "Are you sure you want to delete these?"
    }

}













