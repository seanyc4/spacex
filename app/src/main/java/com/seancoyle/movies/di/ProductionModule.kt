package com.seancoyle.movies.di

import androidx.room.Room
import com.seancoyle.movies.framework.datasource.cache.database.Database
import com.seancoyle.movies.framework.presentation.BaseApplication
import com.seancoyle.movies.util.AndroidTestUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

/**
Dependencies in this class have test fakes for ui tests. See "TestModule.kt" in
androidTest dir
 */
@Module
@InstallIn(SingletonComponent::class)
object ProductionModule {

    @Singleton
    @Provides
    fun provideAndroidTestUtils(): AndroidTestUtils {
        return AndroidTestUtils(false)
    }

    @Singleton
    @Provides
    fun provideMovieDb(app: BaseApplication): Database {
        return Room
            .databaseBuilder(app, Database::class.java, Database.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}












