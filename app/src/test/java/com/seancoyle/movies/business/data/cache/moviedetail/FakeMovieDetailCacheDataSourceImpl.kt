package com.seancoyle.movies.business.data.cache.moviedetail

import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity

const val FORCE_DELETE_MOVIE_CAST_EXCEPTION = -2
const val FORCE_DELETES_MOVIE_CAST_EXCEPTION = -3
const val FORCE_NEW_MOVIE_CAST_EXCEPTION = -4
const val FORCE_GENERAL_FAILURE = -5

class FakeMovieDetailCacheDataSourceImpl
constructor(
    private val fakeMovieDetailDatabase: FakeMovieDetailDatabase
) : MovieDetailCacheDataSource {

    override suspend fun insert(movieCast: MovieCastDomainEntity): Long {
        if (movieCast.id == FORCE_NEW_MOVIE_CAST_EXCEPTION) {
            throw Exception("Something went wrong inserting the movie cast.")
        }
        if (movieCast.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        fakeMovieDetailDatabase.movieCast.add(movieCast)
        return 1 // success
    }

    override suspend fun deleteById(id: Int): Int {
        if (id == FORCE_DELETE_MOVIE_CAST_EXCEPTION) {
            throw Exception("Something went wrong deleting the movie cast.")
        } else if (id == FORCE_DELETES_MOVIE_CAST_EXCEPTION) {
            throw Exception("Something went wrong deleting the movie cast.")
        }

        return if (fakeMovieDetailDatabase.movieCast.removeIf { it.id == id }) {
            1 // return 1 for success
        } else {
            -1 // -1 for failure
        }
    }

    override suspend fun deleteList(castList: List<MovieCastDomainEntity>): Int {
        var failOrSuccess = 1
        for (cast in castList) {
            failOrSuccess = if (fakeMovieDetailDatabase.movieCast.removeIf { it.id == cast.id }) {
                1 // return 1 for success
            } else {
                -1 // mark for failure
            }
        }
        return failOrSuccess
    }

    override suspend fun deleteAll() {
        fakeMovieDetailDatabase.movieCast.clear()
    }

    override suspend fun getById(id: Int): MovieCastDomainEntity? {
        return fakeMovieDetailDatabase.movieCast.find { it.id == id }
    }

    override suspend fun getAll(): List<MovieCastDomainEntity> {
        return fakeMovieDetailDatabase.movieCast
    }

    override suspend fun getTotalEntries(): Int {
        return fakeMovieDetailDatabase.movieCast.size
    }

    override suspend fun insertList(castList: List<MovieCastDomainEntity>): LongArray {
        val results = LongArray(castList.size)
        for (movie in castList.withIndex()) {
            results[movie.index] = 1
            fakeMovieDetailDatabase.movieCast.add(movie.value)
        }
        return results
    }
}















