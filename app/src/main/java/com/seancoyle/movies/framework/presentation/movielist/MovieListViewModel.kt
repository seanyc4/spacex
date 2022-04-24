package com.seancoyle.movies.framework.presentation.movielist

import android.os.Parcelable
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.interactors.movielist.MovieListInteractors
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.framework.presentation.common.BaseViewModel
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

const val MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY = "selectedMovie"
const val MOVIE_DETAIL_ERROR_RETRIEVEING_SELECTED_MOVIE = "Error retrieving selected movie from bundle."


@ExperimentalCoroutinesApi
@FlowPreview
@Singleton
class MovieListViewModel
@Inject
constructor(
    private val movieListInteractors: MovieListInteractors,
    private val movieListFactory: MovieListFactory
) : BaseViewModel<MovieListViewState>() {

    init {
        setStateEvent(GetMoviesFromNetworkAndInsertToCacheEvent)
    }

    override fun handleNewData(data: MovieListViewState) {

        data.let { viewState ->
            viewState.movieParents?.let { movies ->
                setMovieListData(movies)
            }

            viewState.numMoviesInCache?.let { numMovies ->
                setNumMoviesInCache(numMovies)
            }

            viewState.movieParent?.let { movie ->
                setMovieParent(movie)
            }
        }

    }

    override fun setStateEvent(stateEvent: StateEvent) {

        val job: Flow<DataState<MovieListViewState>?> = when (stateEvent) {

            is GetMoviesFromNetworkAndInsertToCacheEvent -> {
                movieListInteractors.getMoviesFromNetworkAndInsertToCache.execute(
                    stateEvent = stateEvent
                )
            }

            is GetMoviesFromCacheEvent -> {
                movieListInteractors.getAllMoviesFromCache.execute(
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

    override fun initNewViewState(): MovieListViewState {
        return MovieListViewState()
    }

    private fun setMovieListData(movieParents: ArrayList<MovieParent>) {
        val update = getCurrentViewStateOrNew()
        update.movieParents = movieParents
        setViewState(update)
    }

    fun setMovieParent(movieParent: MovieParent?) {
        val update = getCurrentViewStateOrNew()
        update.movieParent = movieParent
        setViewState(update)
    }

    fun getMovieParent() = getCurrentViewStateOrNew().movieParent

    fun setMovie(movies: Movie?) {
        val update = getCurrentViewStateOrNew()
        update.movie = movies
        setViewState(update)
    }

    fun getMovie() = getCurrentViewStateOrNew().movie

    private fun setNumMoviesInCache(numMovies: Int) {
        val update = getCurrentViewStateOrNew()
        update.numMoviesInCache = numMovies
        setViewState(update)
    }

    fun getMoviesListSize() = getCurrentViewStateOrNew().movieParents?.size ?: 0

    private fun getNumMoviesInCache() = getCurrentViewStateOrNew().numMoviesInCache ?: 0

    // for debugging
    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun clearList() {
        val update = getCurrentViewStateOrNew()
        update.movieParents = ArrayList()
        setViewState(update)
    }

    fun retrieveNumMoviesInCache() {
        setStateEvent(GetNumMoviesInCacheEvent)
    }

    fun getLayoutManagerState(): Parcelable? {
        return getCurrentViewStateOrNew().layoutManagerState
    }

    fun setLayoutManagerState(layoutManagerState: Parcelable) {
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = layoutManagerState
        setViewState(update)
    }

    fun clearLayoutManagerState() {
        val update = getCurrentViewStateOrNew()
        update.layoutManagerState = null
        setViewState(update)
    }

}












































