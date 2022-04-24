package com.seancoyle.movies.framework.presentation.moviedetail

import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.domain.util.DateUtil
import com.seancoyle.movies.business.interactors.moviedetail.MovieDetailInteractors
import com.seancoyle.movies.framework.presentation.common.BaseViewModel
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailStateEvent
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
class MovieDetailViewModel
@Inject
constructor(
    private val movieDetailInteractors: MovieDetailInteractors,
    val dateUtil: DateUtil
) : BaseViewModel<MovieDetailViewState>() {

    override fun handleNewData(data: MovieDetailViewState) {

        data.let { viewState ->
            viewState.movieCast?.let { cast ->
                setMovieCast(cast)
            }
            viewState.movie?.let { movie ->
                setMovie(movie)
            }
        }

    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<MovieDetailViewState>?> = when (stateEvent) {

            is MovieDetailStateEvent.GetMovieCastFromNetworkAndInsertToCacheEvent -> {
                movieDetailInteractors.getMoviesCastFromNetworkAndInsertToCache.execute(
                    movieId = stateEvent.movieId,
                    stateEvent = stateEvent
                )
            }

            is MovieDetailStateEvent.GetMovieCastByIdFromCacheEvent -> {
                movieDetailInteractors.getMovieCastByIdFromCache.execute(
                    id = stateEvent.id,
                    stateEvent = stateEvent
                )
            }

            is MovieDetailStateEvent.GetAllMovieCastsFromCacheEvent -> {
                movieDetailInteractors.getAllMovieCastFromCache.execute(
                    stateEvent = stateEvent
                )
            }

            is CreateStateMessageEvent -> {
                emitStateMessageEvent(
                    stateMessage = stateEvent.stateMessage,
                    stateEvent = stateEvent
                )
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }
        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): MovieDetailViewState {
        return MovieDetailViewState()
    }

    private fun setMovieCast(movieCast: MovieCast) {
        val update = getCurrentViewStateOrNew()
        update.movieCast = movieCast
        setViewState(update)
    }

    fun getMovieCast() = getCurrentViewStateOrNew().movieCast

    fun setMovie(movie: Movie) {
        val update = getCurrentViewStateOrNew()
        update.movie = movie
        setViewState(update)
    }

    fun getMovie() = getCurrentViewStateOrNew().movie

    // for debugging
    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun clearData() {
        val update = getCurrentViewStateOrNew()
        update.movieCast = null
        setViewState(update)
    }

}












































