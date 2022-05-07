package com.seancoyle.movies.di

import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.data.network.abstraction.movielist.MovieListNetworkDataSource
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.interactors.movielist.*
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [MovieListInteractorsModule::class]
)
object TestMovieListInteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideMovieListInteractors(
        movieListCacheDataSource: MovieListCacheDataSource,
        movieListNetworkDataSource: MovieListNetworkDataSource,
        movieListFactory: MovieListFactory
    ): MovieListInteractors {
        return MovieListInteractors(
            deleteMovie = DeleteMovie(
                cacheDataSource = movieListCacheDataSource
            ),
            deleteMultipleMovies = DeleteMultipleMovies(
                cacheDataSource = movieListCacheDataSource
            ),
            getAllMoviesFromCache = GetAllMoviesFromCache(
                cacheDataSource = movieListCacheDataSource
            ),
            getMovieByIdFromCache = GetMovieByIdFromCache(
                cacheDataSource = movieListCacheDataSource
            ),
            getMoviesFromNetwork = GetMoviesFromNetwork(
                movieListNetworkDataSource = movieListNetworkDataSource
            ),
            getMoviesFromNetworkAndInsertToCache = GetMoviesFromNetworkAndInsertToCache(
                cacheDataSource = movieListCacheDataSource,
                movieListNetworkDataSource = movieListNetworkDataSource,
                factory = movieListFactory
            ),
            getNumMovies = GetNumMovies(
                cacheDataSource = movieListCacheDataSource
            ),
            insertMovie = InsertMovie(
                cacheDataSource = movieListCacheDataSource,
                factory = movieListFactory
            ),
        )
    }

}