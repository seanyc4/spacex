package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertMovie(
    private val cacheDataSource: MovieListCacheDataSource,
    private val factory: MovieListFactory
) {

    fun execute(
        id: String? = null,
        category: String?,
        page: Int?,
        movies: List<Movie>?,
        total_pages: Int?,
        total_results: Int?,
        stateEvent: StateEvent
    ): Flow<DataState<MovieListViewState>?> = flow {

        val newMovie = factory.createSingleMovie(
            id = id,
            category = category,
            page = page,
            movies = movies,
            total_pages = total_pages,
            total_results = total_results
        )

        val cacheResult = safeCacheCall(IO) {
            cacheDataSource.insert(newMovie)
        }

        val cacheResponse = object : CacheResponseHandler<MovieListViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<MovieListViewState> {
                return if (resultObj > 0) {
                    val viewState =
                        MovieListViewState(
                            movies = newMovie
                        )
                    DataState.data(
                        response = Response(
                            message = INSERT_MOVIE_SUCCESS,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Success
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = INSERT_MOVIE_FAILED,
                            uiComponentType = UIComponentType.Toast,
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

    companion object {
        val INSERT_MOVIE_SUCCESS = "Successfully inserted new movie."
        val INSERT_MOVIE_FAILED = "Failed to insert new movie."
    }
}