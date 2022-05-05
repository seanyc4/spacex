package com.seancoyle.movies.framework.presentation.movielist

import android.os.Parcelable
import com.seancoyle.movies.business.domain.model.movielist.Movie
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.interactors.movielist.MovieListInteractors
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.framework.presentation.common.BaseViewModel
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListStateEvent.*
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val MOVIE_DETAIL_SELECTED_MOVIE_BUNDLE_KEY = "selectedMovie"
const val MOVIE_DETAIL_ERROR_RETRIEVEING_SELECTED_MOVIE = "Error retrieving selected movie from bundle."


@ExperimentalCoroutinesApi
@FlowPreview
@HiltViewModel
class MovieListViewModel
@Inject
constructor(
    private val movieListInteractors: MovieListInteractors
) : BaseViewModel<MovieListViewState>() {

    init {
        setStateEvent(GetMoviesFromNetworkAndInsertToCacheEvent)
    }

    override fun handleNewData(data: MovieListViewState) {

        data.let { viewState ->
            viewState.movieList?.let { movies ->
                setMovieList(movies)
            }

            viewState.numMoviesInCache?.let { numMovies ->
                setNumMoviesInCache(numMovies)
            }

            viewState.movies?.let { movie ->
                setMovies(movie)
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

            is GetAllMoviesFromCacheEvent -> {
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

    private fun setMovieList(movieDomainEntities: ArrayList<MoviesDomainEntity>) {
        val update = getCurrentViewStateOrNew()
        update.movieList = movieDomainEntities
        setViewState(update)
    }

    fun getMovieList() = getCurrentViewStateOrNew().movieList

    private fun setMovies(movies: MoviesDomainEntity?) {
        val update = getCurrentViewStateOrNew()
        update.movies = movies
        setViewState(update)
    }

    fun getMovies() = getCurrentViewStateOrNew().movies

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

    fun getMoviesListSize() = getCurrentViewStateOrNew().movieList?.size ?: 0

    private fun getNumMoviesInCache() = getCurrentViewStateOrNew().numMoviesInCache ?: 0

    // for debugging
    fun getActiveJobs() = dataChannelManager.getActiveJobs()

    fun clearList() {
        val update = getCurrentViewStateOrNew()
        update.movieList = ArrayList()
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












































