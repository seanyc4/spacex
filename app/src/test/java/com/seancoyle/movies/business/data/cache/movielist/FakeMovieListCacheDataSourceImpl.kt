package com.seancoyle.movies.business.data.cache.movielist

import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MovieParent
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val FORCE_DELETE_MOVIE_EXCEPTION = "FORCE_DELETE_MOVIE_EXCEPTION"
const val FORCE_DELETES_MOVIE_EXCEPTION = "FORCE_DELETES_MOVIE_EXCEPTION"
const val FORCE_NEW_MOVIE_EXCEPTION = "FORCE_NEW_MOVIE_EXCEPTION"
const val FORCE_GENERAL_FAILURE = "FORCE_GENERAL_FAILURE"

class FakeMovieListCacheDataSourceImpl
constructor(
    private val moviesData: MutableMap<String, MovieParent>
) : MovieListCacheDataSource {

    override suspend fun insert(movie: MovieParent): Long {
        if (movie.id == FORCE_NEW_MOVIE_EXCEPTION) {
            throw Exception("Something went wrong inserting the movie.")
        }
        if (movie.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        moviesData[movie.id] = movie
        return 1 // success
    }

    override suspend fun deleteList(movies: List<MovieParent>): Int {
        var failOrSuccess = 1
        for (movie in movies) {
            if (moviesData.remove(movie.id) == null) {
                failOrSuccess = -1 // mark for failure
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
        return moviesData.remove(id)?.let {
            1 // return 1 for success
        } ?: -1 // -1 for failure
    }

    override suspend fun deleteAll() {
        moviesData.clear()
    }

    override suspend fun getById(id: String): MovieParent? {
        return moviesData[id]
    }

    override suspend fun getAll(): List<MovieParent> {
        return ArrayList(moviesData.values)
    }

    override suspend fun getTotalEntries(): Int {
        return moviesData.size
    }

    override suspend fun insertMovies(movies: List<MovieParent>): LongArray {
        val results = LongArray(movies.size)
        for ((index, movie) in movies.withIndex()) {
            results[index] = 1
            moviesData[movie.id] = movie
        }
        return results
    }
}















