package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumMovies(
    private val cacheDataSource: MovieListCacheDataSource
) {

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<MovieListViewState>?> = flow {

        val cacheResult = safeCacheCall(IO) {
            cacheDataSource.getTotalEntries()
        }
        val response = object : CacheResponseHandler<MovieListViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Int): DataState<MovieListViewState>? {
                val viewState = MovieListViewState(
                    numMoviesInCache = resultObj
                )
                return DataState.data(
                    response = Response(
                        message = GET_NUM_MOVIES_SUCCESS,
                        uiComponentType = UIComponentType.None,
                        messageType = MessageType.Success
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object {
        val GET_NUM_MOVIES_SUCCESS = "Successfully retrieved the number of movies from the cache."
        val GET_NUM_MOVIES_FAILED = "Failed to get the number of movies from the cache."
    }
}