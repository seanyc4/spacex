package com.seancoyle.movies.business.data.network.movielist

import com.google.gson.GsonBuilder
import com.seancoyle.movies.business.data.network.abstraction.movielist.MovieListNetworkDataSource
import com.seancoyle.movies.business.domain.model.movielist.MoviesDomainEntity
import com.seancoyle.movies.framework.datasource.network.api.movielist.MovieListApi
import com.seancoyle.movies.framework.datasource.network.mappers.movielist.MovieListNetworkMapper
import com.seancoyle.movies.util.Constants
import okhttp3.HttpUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeMovieListNetworkDataSourceImpl
constructor(
    private val baseUrl: HttpUrl,
    private val networkMapper: MovieListNetworkMapper
) : MovieListNetworkDataSource {

    private val api: MovieListApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(MovieListApi::class.java)


    override suspend fun get(): MoviesDomainEntity {
        return networkMapper.mapFromEntity(
            api.get(
                apiKey = Constants.API_KEY,
                language = "en-US",
                sortBy = "primary_release_date.desc",
                page = "1",
                withCompanies = "420|19551|38679|2301|13252"
            )
        )
    }

}
