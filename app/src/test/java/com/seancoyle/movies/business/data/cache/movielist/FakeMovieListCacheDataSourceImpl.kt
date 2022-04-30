package com.seancoyle.movies.business.data.cache.movielist

import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity

const val FORCE_DELETE_MOVIE_EXCEPTION = "FORCE_DELETE_MOVIE_EXCEPTION"
const val FORCE_DELETES_MOVIE_EXCEPTION = "FORCE_DELETES_MOVIE_EXCEPTION"
const val FORCE_NEW_MOVIE_EXCEPTION = "FORCE_NEW_MOVIE_EXCEPTION"
const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

class FakeMovieListCacheDataSourceImpl
constructor(
    private val fakeMovieListDatabase: FakeMovieListDatabase
) : MovieListCacheDataSource {

    override suspend fun insert(movies: MoviesDomainEntity): Long {
        if (movies.id == FORCE_NEW_MOVIE_EXCEPTION) {
            throw Exception("Something went wrong inserting the movies.")
        }
        if (movies.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        fakeMovieListDatabase.movies.add(movies)
        return 1 // success
    }

    override suspend fun deleteList(movies: List<MoviesDomainEntity>): Int {
        var failOrSuccess = 1
        for (movie in movies) {
            failOrSuccess = if (fakeMovieListDatabase.movies.removeIf { it.id == movie.id }) {
                1 // return 1 for success
            } else {
                -1 // mark for failure
            }
        }
        return failOrSuccess
    }

    override suspend fun deleteById(id: String): Int {
        if (id == FORCE_DELETE_MOVIE_EXCEPTION) {
            throw Exception("Something went wrong deleting the movie.")
        } else if (id == FORCE_DELETES_MOVIE_EXCEPTION) {
            throw Exception("Something went wrong deleting the movie.")
        }
        return if (fakeMovieListDatabase.movies.removeIf { it.id == id }) {
            1
        } else {
            -1
        }
    }

    override suspend fun deleteAll() {
        fakeMovieListDatabase.movies.clear()
    }

    override suspend fun getById(id: String): MoviesDomainEntity? {
        return fakeMovieListDatabase.movies.find { it.id == id }
    }

    override suspend fun getAll(): List<MoviesDomainEntity> {
        return fakeMovieListDatabase.movies
    }

    override suspend fun getTotalEntries(): Int {
        return fakeMovieListDatabase.movies.size
    }

    override suspend fun insertMovies(movies: List<MoviesDomainEntity>): LongArray {
        val results = LongArray(movies.size)
        for (movie in movies.withIndex()) {
            results[movie.index] = 1
            fakeMovieListDatabase.movies.add(movie.value)
        }
        return results
    }
}















