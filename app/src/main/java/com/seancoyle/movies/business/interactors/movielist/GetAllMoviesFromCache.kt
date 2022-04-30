package com.seancoyle.movies.business.interactors.movielist

import com.seancoyle.movies.business.data.cache.CacheResponseHandler
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.business.domain.state.*
import com.seancoyle.movies.business.data.util.safeCacheCall
import com.seancoyle.movies.framework.presentation.movielist.state.MovieListViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllMoviesFromCache(
    private val cacheDataSource: MovieListCacheDataSource
){

    fun execute(
        stateEvent: StateEvent
    ): Flow<DataState<MovieListViewState>?> = flow {

        val cacheResult = safeCacheCall(Dispatchers.IO){
            cacheDataSource.getAll()
        }

        val response = object: CacheResponseHandler<MovieListViewState, List<MoviesDomainEntity>?>(
            response = cacheResult,
            stateEvent = stateEvent
        ){
            override suspend fun handleSuccess(resultObj: List<MoviesDomainEntity>?): DataState<MovieListViewState> {
                var message: String? =
                    GET_ALL_MOVIES_SUCCESS
                var uiComponentType: UIComponentType? = UIComponentType.None
                if(resultObj == null){
                    message =
                        GET_ALL_MOVIES_NO_MATCHING_RESULTS
                    uiComponentType = UIComponentType.Toast
                }
                return DataState.data(
                    response = Response(
                        message = message,
                        uiComponentType = uiComponentType as UIComponentType,
                        messageType = MessageType.Success
                    ),
                    data = MovieListViewState(
                        movieList = resultObj as ArrayList<MoviesDomainEntity>?
                    ),
                    stateEvent = stateEvent
                )
            }


        }.getResult()

        emit(response)
    }

    companion object{
        val GET_ALL_MOVIES_SUCCESS = "Successfully retrieved all movies"
        val GET_ALL_MOVIES_NO_MATCHING_RESULTS = "There are no movies that match that query."
        val GET_ALL_MOVIES_FAILED = "Failed to retrieve all movies."
        val NO_DATA = "Error getting all movies from cache.\n\nReason: Cache data is null."

    }
}







