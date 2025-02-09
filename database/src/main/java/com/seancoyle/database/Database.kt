package com.seancoyle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.database.dao.CompanyDao
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.entities.CompanyEntity
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.util.LocalDateTimeConverter

@Database(
    entities =
    [
        LaunchEntity::class,
        CompanyEntity::class
    ],
    version = 4,
    exportSchema = true
)
@TypeConverters(
    LocalDateTimeConverter::class
)
abstract class Database : RoomDatabase() {

    abstract fun launchDao(): LaunchDao
    abstract fun companyInfoDao(): CompanyDao

    companion object {
        const val DATABASE_NAME: String = "spacex_db"
    }

}