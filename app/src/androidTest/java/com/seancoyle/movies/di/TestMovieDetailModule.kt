package com.seancoyle.movies.di

import com.seancoyle.movies.business.data.cache.abstraction.moviedetail.MovieDetailCacheDataSource
import com.seancoyle.movies.business.data.cache.implementation.moviedetail.MovieDetailCacheDataSourceImpl
import com.seancoyle.movies.business.data.network.abstraction.moviedetail.MovieDetailNetworkDataSource
import com.seancoyle.movies.business.data.network.implementation.moviedetail.MovieDetailNetworkDataSourceImpl
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.framework.datasource.cache.abstraction.moviedetail.MovieDetailDaoService
import com.seancoyle.movies.framework.datasource.cache.database.Database
import com.seancoyle.movies.framework.datasource.cache.dao.moviedetail.MovieDetailDao
import com.seancoyle.movies.framework.datasource.cache.implementation.moviedetail.MovieDetailDaoServiceImpl
import com.seancoyle.movies.framework.datasource.cache.mappers.moviedetail.MovieDetailCacheMapper
import com.seancoyle.movies.framework.datasource.network.abstraction.moviedetail.MovieDetailRetrofitService
import com.seancoyle.movies.framework.datasource.network.api.moviedetail.MovieDetailApi
import com.seancoyle.movies.framework.datasource.network.implementation.moviedetail.MovieCastRetrofitServiceImpl
import com.seancoyle.movies.framework.datasource.network.mappers.moviedetail.MovieDetailNetworkMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [MovieDetailModule::class]
)
object TestMovieDetailModule {

    @Singleton
    @Provides
    fun provideMovieDetailNetworkDataSource(
        movieDetailRetrofitService: MovieDetailRetrofitService
    ): MovieDetailNetworkDataSource {
        return MovieDetailNetworkDataSourceImpl(
            retrofitService = movieDetailRetrofitService
        )
    }

    @Singleton
    @Provides
    fun provideMovieDetailRetrofitService(
        api: MovieDetailApi,
        networkMapper: MovieDetailNetworkMapper
    ): MovieDetailRetrofitService {
        return MovieCastRetrofitServiceImpl(
            api = api,
            networkMapper = networkMapper
        )
    }

    @Singleton
    @Provides
    fun provideMovieDetailApi(
        retrofit: Retrofit
    ): MovieDetailApi {
        return retrofit.create(MovieDetailApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieDetailNetworkMapper(): MovieDetailNetworkMapper {
        return MovieDetailNetworkMapper()
    }

    @Singleton
    @Provides
    fun provideMovieDetailFactory(): MovieDetailFactory {
        return MovieDetailFactory()
    }

    @Singleton
    @Provides
    fun provideMovieDetailDao(database: Database): MovieDetailDao {
        return database.movieDetailDao()
    }

    @Singleton
    @Provides
    fun provideMovieDetailCacheMapper(): MovieDetailCacheMapper {
        return MovieDetailCacheMapper()
    }

    @Singleton
    @Provides
    fun provideMovieDetailDaoService(
        dao: MovieDetailDao,
        movieDetailCacheMapper: MovieDetailCacheMapper
    ): MovieDetailDaoService {
        return MovieDetailDaoServiceImpl(
            dao = dao,
            cacheMapper = movieDetailCacheMapper
        )
    }

    @Singleton
    @Provides
    fun provideMovieDetailCacheDataSource(
        daoService: MovieDetailDaoService
    ): MovieDetailCacheDataSource {
        return MovieDetailCacheDataSourceImpl(
            daoService = daoService
        )
    }

}