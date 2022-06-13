package com.seancoyle.spacex.di

import android.content.Context
import androidx.room.Room
import com.seancoyle.core.datastore.AppDataStore
import com.seancoyle.core.datastore.AppDataStoreManager
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
        application: HiltTestApplication
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















