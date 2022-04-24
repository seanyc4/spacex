package com.seancoyle.movies.framework.presentation.movielist.state

import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.state.StateEvent
import com.seancoyle.movies.business.domain.state.StateMessage

sealed class MovieListStateEvent : StateEvent {

    object GetMoviesFromNetworkAndInsertToCacheEvent : MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error getting movies from network."
        }

        override fun eventName(): String {
            return "GetMoviesFromNetworkAndInsertToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class InsertNewMovieEvent(
        val id: String?,
        val category: String?,
        val page: Int?,
        val movies: List<Movie>?,
        val total_pages: Int?,
        val total_results: Int?
    ) : MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting new movie."
        }

        override fun eventName(): String {
            return "InsertNewMovieEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    // for testing
    class InsertMultipleMoviesEvent(
        val numMovies: Int
    ) : MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting the movies."
        }

        override fun eventName(): String {
            return "InsertMultipleMoviesEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class DeleteMovieEvent(
        val movieParent: MovieParent
    ) : MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error deleting movie."
        }

        override fun eventName(): String {
            return "DeleteMovieEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetMoviesFromCacheEvent : MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error getting list of movies."
        }

        override fun eventName(): String {
            return "GetMoviesFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetMovieByIdFromCacheEvent(
        val id: String
    ): MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error getting movie by id."
        }

        override fun eventName(): String {
            return "GetMovieByIdFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetAllMoviesFromCacheEvent: MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error getting all movies."
        }

        override fun eventName(): String {
            return "GetAllMoviesFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetNumMoviesInCacheEvent : MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error getting the number of movies from the cache."
        }

        override fun eventName(): String {
            return "GetNumMoviesInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ) : MovieListStateEvent() {

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















