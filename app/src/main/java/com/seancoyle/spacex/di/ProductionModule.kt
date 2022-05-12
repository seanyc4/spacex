package com.seancoyle.spacex.di

import androidx.room.Room
import com.seancoyle.spacex.framework.datasource.cache.database.Database
import com.seancoyle.spacex.framework.presentation.BaseApplication
import com.seancoyle.spacex.util.AndroidTestUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
    fun provideSpaceXDb(app: BaseApplication): Database {
        return Room
            .databaseBuilder(app, Database::class.java, Database.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}












