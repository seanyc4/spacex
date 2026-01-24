package com.seancoyle.database

import android.content.Context
import androidx.room.Room
import com.seancoyle.database.di.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Singleton
    @Provides
    fun provideLaunchesDb(@ApplicationContext app: Context): Database {
        return Room
            .inMemoryDatabaseBuilder(app, Database::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }
}