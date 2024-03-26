package com.seancoyle.core_database.implementation

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.core_database.api.CompanyDao
import com.seancoyle.core_database.api.CompanyEntity
import com.seancoyle.core_database.api.LaunchDao
import com.seancoyle.core_database.api.LaunchEntity
import com.seancoyle.core_database.api.typeconverters.LocalDateTimeTypeConverter

@Database(
    entities =
    [
        LaunchEntity::class,
        CompanyEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(
    LocalDateTimeTypeConverter::class
)
internal abstract class Database : RoomDatabase() {

    abstract fun launchDao(): LaunchDao
    abstract fun companyInfoDao(): CompanyDao

    companion object {
        const val DATABASE_NAME: String = "spacex_db"
    }

}