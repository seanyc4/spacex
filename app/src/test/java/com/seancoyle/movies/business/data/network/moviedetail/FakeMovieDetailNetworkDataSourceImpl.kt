package com.seancoyle.movies.business.data.network.moviedetail

import com.google.gson.GsonBuilder
import com.seancoyle.movies.business.data.network.abstraction.moviedetail.MovieDetailNetworkDataSource
import com.seancoyle.movies.business.domain.model.moviedetail.MovieCastDomainEntity
import com.seancoyle.movies.framework.datasource.network.api.moviedetail.MovieDetailApi
import com.seancoyle.movies.framework.datasource.network.mappers.moviedetail.MovieDetailNetworkMapper
import com.seancoyle.movies.util.Constants.API_KEY
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeMovieDetailNetworkDataSourceImpl
constructor(
    private val baseUrl: HttpUrl,
    private val networkMapper: MovieDetailNetworkMapper
) : MovieDetailNetworkDataSource {

    private val api: MovieDetailApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(MovieDetailApi::class.java)


    override suspend fun getCast(movieId: String): MovieCastDomainEntity {
        return networkMapper.mapFromEntity(
            api.getCast(
                movieId = "447365" ,
                apiKey = API_KEY,
                language = "en-US"
            )
        )
    }

}
