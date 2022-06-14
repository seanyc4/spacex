package com.seancoyle.launch_datasource.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.launch_datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.launch_datasource.cache.dao.launch.LaunchDao
import com.seancoyle.launch_datasource.cache.entities.company.CompanyInfoEntity
import com.seancoyle.launch_datasource.cache.entities.launch.LaunchEntity
import com.seancoyle.launch_datasource.cache.typeconverters.LocalDateTimeTypeConverter

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