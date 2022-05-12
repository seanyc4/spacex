package com.seancoyle.spacex.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seancoyle.spacex.framework.datasource.cache.dao.company.CompanyInfoDao
import com.seancoyle.spacex.framework.datasource.cache.dao.launch.LaunchDao
import com.seancoyle.spacex.framework.datasource.cache.model.launch.LaunchCacheEntity
import com.seancoyle.spacex.framework.datasource.cache.model.company.CompanyInfoCacheEntity

@Database(
    entities =
    [
        LaunchCacheEntity::class,
        CompanyInfoCacheEntity::class
    ],
    version = 1
)
abstract class Database : RoomDatabase() {

    abstract fun launchDao(): LaunchDao
    abstract fun companyInfoDao(): CompanyInfoDao

    companion object {
        const val DATABASE_NAME: String = "spacex_db"
    }

}