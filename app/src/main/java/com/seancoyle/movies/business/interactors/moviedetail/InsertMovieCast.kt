package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.business.domain.model.moviedetail.Cast
import com.seancoyle.movies.business.domain.model.moviedetail.Crew
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertMovieCast(
    private val movieDetailCacheDataSource: MovieDetailCacheDataSource,
    private val movieDetailFactory: MovieDetailFactory
) {

    fun execute(
        id: Int?,
        crewList: List<Crew>,
        castList: List<Cast>,
        stateEvent: StateEvent
    ): Flow<DataState<MovieDetailViewState>?> = flow {

        val newMovieCast = movieDetailFactory.createSingleMovieCast(
            id = id,
            crewList = crewList,
            castList = castList
        )

        val cacheResult = safeCacheCall(IO) {
            movieDetailCacheDataSource.insert(newMovieCast)
        }

        val cacheResponse = object : CacheResponseHandler<MovieDetailViewState, Long>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: Long): DataState<MovieDetailViewState> {
                return if (resultObj > 0) {
                    val viewState =
                        MovieDetailViewState(
                            movieCast = newMovieCast
                        )
                    DataState.data(
                        response = Response(
                            message = INSERT_MOVIE_CAST_SUCCESS,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Success
                        ),
                        data = viewState,
                        stateEvent = stateEvent
                    )
                } else {
                    DataState.data(
                        response = Response(
                            message = INSERT_MOVIE_CAST_FAILED,
                            uiComponentType = UIComponentType.Toast,
                            messageType = MessageType.Error
                        ),
                        data = null,
                        stateEvent = stateEvent
                    )
                }
            }
        }.getResult()

        emit(cacheResponse)
    }

    companion object {
        val INSERT_MOVIE_CAST_SUCCESS = "Successfully inserted new movie cast."
        val INSERT_MOVIE_CAST_FAILED = "Failed to insert new movie cast."
    }
}