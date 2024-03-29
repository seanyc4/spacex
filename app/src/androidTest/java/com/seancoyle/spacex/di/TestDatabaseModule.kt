package com.seancoyle.spacex.di

import android.content.Context
import androidx.room.Room
import com.seancoyle.core_database.implementation.Database
import com.seancoyle.core_database.implementation.DatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {
    @Provides
    fun provideSpaceXDb(@ApplicationContext app: Context): Database {
        return Room
            .inMemoryDatabaseBuilder(app, Database::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }
}