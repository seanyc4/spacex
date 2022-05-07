package com.seancoyle.movies.di


import androidx.room.Room
import com.seancoyle.movies.business.domain.model.moviedetail.MovieDetailFactory
import com.seancoyle.movies.business.domain.model.movielist.MovieListFactory
import com.seancoyle.movies.framework.datasource.cache.database.Database
import com.seancoyle.movies.framework.datasource.data.moviedetail.MovieDetailDataFactory
import com.seancoyle.movies.framework.datasource.data.movielist.MovieListDataFactory
import com.seancoyle.movies.util.AndroidTestUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ProductionModule::class]
)
object TestModule {

    @Singleton
    @Provides
    fun provideAndroidTestUtils(): AndroidTestUtils {
        return AndroidTestUtils(true)
    }

    @Singleton
    @Provides
    fun provideMovieDb(app: HiltTestApplication): Database {
        return Room
            .inMemoryDatabaseBuilder(app, Database::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieListDataFactory(
        application: HiltTestApplication,
        movieListFactory: MovieListFactory
    ): MovieListDataFactory {
        return MovieListDataFactory(application, movieListFactory)
    }

    @Singleton
    @Provides
    fun provideMovieDetailDataFactory(
        application: HiltTestApplication,
        movieDetailFactory: MovieDetailFactory
    ): MovieDetailDataFactory {
        return MovieDetailDataFactory(application, movieDetailFactory)
    }

}















