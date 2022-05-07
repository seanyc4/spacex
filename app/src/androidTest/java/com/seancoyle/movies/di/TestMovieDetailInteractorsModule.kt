package com.seancoyle.movies.di

import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.data.network.abstraction.moviedetail.MovieDetailNetworkDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.interactors.moviedetail.*
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [MovieDetailInteractorsModule::class]
)
object TestMovieDetailInteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideMovieDetailInteractors(
        movieDetailCacheDataSource: MovieDetailCacheDataSource,
        movieDetailNetworkDataSource: MovieDetailNetworkDataSource,
        movieDetailFactory: MovieDetailFactory
    ): MovieDetailInteractors {
        return MovieDetailInteractors(
            deleteMovieCast = DeleteMovieCast(
                cacheDataSource = movieDetailCacheDataSource
            ),
            deleteMultipleMovieCasts = DeleteMultipleMovieCasts(
                cacheDataSource = movieDetailCacheDataSource
            ),
            getAllMovieCastFromCache = GetAllMovieCastFromCache(
                cacheDataSource = movieDetailCacheDataSource
            ),
            getMovieCastByIdFromCache = GetMovieCastByIdFromCache(
                cacheDataSource = movieDetailCacheDataSource
            ),
            getMoviesCastFromNetworkAndInsertToCache = GetMovieCastFromNetworkAndInsertToCache(
                cacheDataSource = movieDetailCacheDataSource,
                networkDataSource = movieDetailNetworkDataSource,
                factory = movieDetailFactory
            ),
            getNumMoviesCast = GetNumMoviesCast(
                cacheDataSource = movieDetailCacheDataSource
            ),
            insertMovieCast = InsertMovieCast(
                movieDetailCacheDataSource = movieDetailCacheDataSource,
                movieDetailFactory = movieDetailFactory
            )
        )
    }

}