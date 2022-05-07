package com.seancoyle.movies.di

import com.google.gson.GsonBuilder
import com.seancoyle.movies.business.data.cache.abstraction.movielist.MovieListCacheDataSource
import com.seancoyle.movies.business.data.cache.implementation.movielist.MovieListCacheDataSourceImpl
import com.seancoyle.movies.business.data.network.abstraction.movielist.MovieListNetworkDataSource
import com.seancoyle.movies.business.data.network.implementation.movielist.MovieListNetworkDataSourceImpl
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.business.domain.util.DateUtil
import com.seancoyle.movies.framework.datasource.cache.abstraction.movielist.MovieListDaoService
import com.seancoyle.movies.framework.datasource.cache.database.Database
import com.seancoyle.movies.framework.datasource.cache.dao.movielist.MovieListDao
import com.seancoyle.movies.framework.datasource.cache.implementation.movielist.MovieListDaoServiceImpl
import com.seancoyle.movies.framework.datasource.cache.mappers.movielist.MovieListCacheMapper
import com.seancoyle.movies.framework.datasource.network.abstraction.movielist.MovieListRetrofitService
import com.seancoyle.movies.framework.datasource.network.api.movielist.MovieListApi
import com.seancoyle.movies.framework.datasource.network.implementation.movielist.MovieListRetrofitServiceImpl
import com.seancoyle.movies.framework.datasource.network.mappers.movielist.MovieListNetworkMapper
import com.seancoyle.movies.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MovieListModule::class]
)
object TestMovieListModule {

    @Singleton
    @Provides
    fun provideMovieListNetworkDataSource(
        movieListRetrofitService: MovieListRetrofitService
    ): MovieListNetworkDataSource {
        return MovieListNetworkDataSourceImpl(
            retrofitService = movieListRetrofitService
        )
    }

    @Singleton
    @Provides
    fun provideMovieListRetrofitService(
        api: MovieListApi,
        networkMapper: MovieListNetworkMapper
    ): MovieListRetrofitService {
        return MovieListRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }

    @Singleton
    @Provides
    fun provideMovieListApi(
        retrofit: Retrofit
    ): MovieListApi {
        return retrofit.create(MovieListApi::class.java)
    }

    @Provides
    fun provideMovieRetrofitBuilder(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieListNetworkMapper(): MovieListNetworkMapper {
        return MovieListNetworkMapper()
    }

    @Singleton
    @Provides
    fun provideMovieListFactory(dateUtil: DateUtil): MovieListFactory {
        return MovieListFactory(
            dateUtil
        )
    }

    @Singleton
    @Provides
    fun provideMovieListDao(database: Database): MovieListDao {
        return database.movieListDao()
    }

    @Singleton
    @Provides
    fun provideMovieListCacheMapper(): MovieListCacheMapper {
        return MovieListCacheMapper()
    }

    @Singleton
    @Provides
    fun provideMovieListDaoService(
        dao: MovieListDao,
        movieListCacheMapper: MovieListCacheMapper
    ): MovieListDaoService {
        return MovieListDaoServiceImpl(
            dao = dao,
            cacheMapper = movieListCacheMapper
        )
    }

    @Singleton
    @Provides
    fun provideMovieListCacheDataSource(
        daoService: MovieListDaoService
    ): MovieListCacheDataSource {
        return MovieListCacheDataSourceImpl(
            daoService = daoService
        )
    }

}