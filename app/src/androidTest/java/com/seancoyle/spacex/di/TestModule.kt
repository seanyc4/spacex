package com.seancoyle.spacex.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.seancoyle.spacex.business.datastore.AppDataStore
import com.seancoyle.spacex.business.datastore.AppDataStoreManager
import com.seancoyle.spacex.framework.datasource.cache.database.Database
import com.seancoyle.spacex.util.AndroidTestUtils
import com.seancoyle.spacex.util.JsonFileReader
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideApplication(@ApplicationContext app: Context): HiltTestApplication {
        return app as HiltTestApplication
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
    fun provideSpaceXDb(app: HiltTestApplication): Database {
        return Room
            .inMemoryDatabaseBuilder(app, Database::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAndroidTestUtils(): AndroidTestUtils {
        return AndroidTestUtils(true)
    }

    @Singleton
    @Provides
    fun providesJsonFileReader(application: HiltTestApplication): JsonFileReader {
        return JsonFileReader(
            application = application
        )
    }

}















