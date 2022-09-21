package com.seancoyle.spacex.di


import android.content.Context
import androidx.room.Room
import com.seancoyle.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideSpaceXDb(@ApplicationContext context: Context): Database {
        return Room
            .databaseBuilder(context, Database::class.java, Database.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

}












