package com.seancoyle.spacex.di

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
    fun provideSpaceXDb(@ApplicationContext app: Context): com.seancoyle.database.Database {
        return Room
            .inMemoryDatabaseBuilder(app, com.seancoyle.database.Database::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }
}