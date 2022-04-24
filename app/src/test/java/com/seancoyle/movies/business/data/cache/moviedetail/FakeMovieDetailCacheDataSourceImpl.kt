package com.seancoyle.movies.business.data.cache.moviedetail

import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCast
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

const val FORCE_DELETE_MOVIE_CAST_EXCEPTION = -2
const val FORCE_DELETES_MOVIE_CAST_EXCEPTION = -3
const val FORCE_NEW_MOVIE_CAST_EXCEPTION = -4
const val FORCE_GENERAL_FAILURE = -5

class FakeMovieDetailCacheDataSourceImpl
constructor(
    private val castData: HashMap<Int, MovieCast>
) : MovieDetailCacheDataSource {

    override suspend fun insert(movieCast: MovieCast): Long {
        if (movieCast.id == FORCE_NEW_MOVIE_CAST_EXCEPTION) {
            throw Exception("Something went wrong inserting the movie cast.")
        }
        if (movieCast.id == FORCE_GENERAL_FAILURE) {
            return -1 // fail
        }
        castData[movieCast.id] = movieCast
        return 1 // success
    }

    override suspend fun deleteById(id: Int): Int {
        if (id == FORCE_DELETE_MOVIE_CAST_EXCEPTION) {
            throw Exception("Something went wrong deleting the movie cast.")
        } else if (id == FORCE_DELETES_MOVIE_CAST_EXCEPTION) {
            throw Exception("Something went wrong deleting the movie cast.")
        }
        return castData.remove(id)?.let {
            1 // return 1 for success
        } ?: -1 // -1 for failure
    }

    override suspend fun deleteList(castList: List<MovieCast>): Int {
        var failOrSuccess = 1
        for (cast in castList) {
            if (castData.remove(cast.id) == null) {
                failOrSuccess = -1 // mark for failure
            }
        }
        return failOrSuccess
    }

    override suspend fun deleteAll() {
        castData.clear()
    }

    override suspend fun getById(id: Int): MovieCast? {
        return castData[id]
    }

    override suspend fun getAll(): List<MovieCast> {
        return ArrayList(castData.values)
    }

    override suspend fun getTotalEntries(): Int {
        return castData.size
    }

    override suspend fun insertList(castList: List<MovieCast>): LongArray {
        val results = LongArray(castList.size)
        for ((index, movie) in castList.withIndex()) {
            results[index] = 1
            castData[movie.id] = movie
        }
        return results
    }
}















