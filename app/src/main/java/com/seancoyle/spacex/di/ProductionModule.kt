package com.seancoyle.spacex.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.seancoyle.core.datastore.AppDataStore
import com.seancoyle.core.datastore.AppDataStoreManager
import com.seancoyle.launch_datasource.cache.database.Database
import com.seancoyle.spacex.framework.presentation.BaseApplication
import com.seancoyle.ui_launch.common.AndroidTestUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

/**
Dependencies in this class have test fakes for ui tests. See "TestModule.kt" in
androidTest dir
 */
@ExperimentalCoroutinesApi
@FlowPreview
@Module
@InstallIn(SingletonComponent::class)
object ProductionModule {


    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideDataStoreManager(
        application: Application
    ): AppDataStore {
        return AppDataStoreManager(application)
    }

    @Singleton
    @Provides
    fun provideSpaceXDb(app: BaseApplication): Database {
        return Room
            .databaseBuilder(app, Database::class.java, Database.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAndroidTestUtils(): AndroidTestUtils {
        return AndroidTestUtils(false)
    }

}












