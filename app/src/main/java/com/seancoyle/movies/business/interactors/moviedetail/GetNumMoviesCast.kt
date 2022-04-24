package com.seancoyle.movies.business.interactors.moviedetail

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.framework.presentation.moviedetail.state.MovieDetailViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumMoviesCast(
    private val cacheDataSource: MovieDetailCacheDataSource
){

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<MovieDetailViewState>?> = flow {

        val cacheResult = safeCacheCall(IO){
            cacheDataSource.getTotalEntries()
        }
        val response =  object: CacheResponseHandler<MovieDetailViewState, Int>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: Int): DataState<MovieDetailViewState> {
                val viewState = MovieDetailViewState(
                    numMoviesCast = resultObj
                )
                return DataState.data(
                    response = Response(
                        message = GET_NUM_MOVIE_CAST_SUCCESS,
                        uiComponentType = UIComponentType.None,
                        messageType = MessageType.Success
                    ),
                    data = viewState,
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }

    companion object{
        val GET_NUM_MOVIE_CAST_SUCCESS = "Successfully retrieved the number of movie casts from the cache."
        val GET_NUM_MOVIE_CAST_FAILED = "Failed to get the number of movie casts from the cache."
    }
}