package com.seancoyle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.LaunchRemoteKeyDao
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.database.util.AgencyListConverter
import com.seancoyle.database.util.CountryListConverter
import com.seancoyle.database.util.LocalDateTimeConverter
import com.seancoyle.database.util.ProgramListConverter

@Database(
    entities =
    [
        LaunchEntity::class,
        LaunchRemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    LocalDateTimeConverter::class,
    AgencyListConverter::class,
    ProgramListConverter::class,
    CountryListConverter::class
)
abstract class Database : RoomDatabase() {

    abstract fun launchDao(): LaunchDao
    abstract fun launchRemoteKeyDao(): LaunchRemoteKeyDao

    companion object {
        const val DATABASE_NAME: String = "launches_db"
    }

}
