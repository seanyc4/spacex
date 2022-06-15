package com.seancoyle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.data.daos.CompanyInfoDao
import com.seancoyle.data.daos.LaunchDao
import com.seancoyle.data.entities.CompanyInfoEntity
import com.seancoyle.data.entities.LaunchEntity
import com.seancoyle.data.typeconverters.LocalDateTimeTypeConverter

@Database(
    entities =
    [
        LaunchEntity::class,
        CompanyInfoEntity::class
    ],
    version = 3
)
@TypeConverters(
    LocalDateTimeTypeConverter::class
)
abstract class Database : RoomDatabase() {

    abstract fun launchDao(): LaunchDao
    abstract fun companyInfoDao(): CompanyInfoDao

    companion object {
        const val DATABASE_NAME: String = "spacex_db"
    }

}