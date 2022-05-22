package com.seancoyle.spacex.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LaunchEntity
import com.seancoyle.spacex.framework.datasource.cache.model.company.CompanyInfoEntity
import com.seancoyle.spacex.framework.datasource.cache.typeconverters.LocalDateTimeTypeConverter

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