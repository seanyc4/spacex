package com.seancoyle.movies.framework.presentation.moviedetail.state

import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.business.domain.model.moviedetail.Crew
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.state.StateEvent
import com.seancoyle.movies.business.domain.state.StateMessage


sealed class MovieDetailStateEvent: StateEvent {

    class GetMovieCastFromNetworkAndInsertToCacheEvent(
        val movieId: String
    ) : MovieDetailStateEvent() {

        override fun errorInfo(): String {
            return "Error getting movie cast from network."
        }

        override fun eventName(): String {
            return "GetMovieCastFromNetworkAndInsertToCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class InsertMovieCastEvent(
        val id: Int,
        val crewList: List<Crew>,
        val castList: List<Cast>
    ): MovieDetailStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting the movie cast."
        }

        override fun eventName(): String {
            return "InsertMovieCastEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    // for testing
    class InsertMultipleMovieCastsEvent(
        val numMovieCast: Int
    ): MovieDetailStateEvent() {

        override fun errorInfo(): String {
            return "Error inserting the movie cast."
        }

        override fun eventName(): String {
            return "InsertMultipleMovieCastsEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class DeleteMovieCastEvent(
        val movieCast: MovieCast
    ): MovieDetailStateEvent(){

        override fun errorInfo(): String {
            return "Error deleting movie cast."
        }

        override fun eventName(): String {
            return "DeleteMovieCastEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class GetMovieCastByIdFromCacheEvent(
        val id: Int
    ) : MovieDetailStateEvent() {

        override fun errorInfo(): String {
            return "Error getting movie cast."
        }

        override fun eventName(): String {
            return "GetMovieCastByIdFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetAllMovieCastsFromCacheEvent : MovieDetailStateEvent() {

        override fun errorInfo(): String {
            return "Error getting movie cast."
        }

        override fun eventName(): String {
            return "GetAllMovieCastsFromCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    object GetNumMovieCastInCacheEvent : MovieDetailStateEvent() {

        override fun errorInfo(): String {
            return "Error getting the number of movie casts from the cache."
        }

        override fun eventName(): String {
            return "GetNumMovieCastInCacheEvent"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): MovieDetailStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















