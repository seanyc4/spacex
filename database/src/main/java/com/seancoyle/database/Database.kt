package com.seancoyle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.database.dao.LaunchDao
import com.seancoyle.database.dao.LaunchRemoteKeyDao
import com.seancoyle.database.entities.LaunchEntity
import com.seancoyle.database.entities.LaunchRemoteKeyEntity
import com.seancoyle.database.entities.LaunchSummaryEntity
import com.seancoyle.database.util.AgencyListConverter
import com.seancoyle.database.util.CountryListConverter
import com.seancoyle.database.util.FamilyListConverter
import com.seancoyle.database.util.InfoUrlListConverter
import com.seancoyle.database.util.LaunchUpdateListConverter
import com.seancoyle.database.util.LauncherStageListConverter
import com.seancoyle.database.util.LocalDateTimeConverter
import com.seancoyle.database.util.MissionPatchListConverter
import com.seancoyle.database.util.ProgramListConverter
import com.seancoyle.database.util.SpacecraftStageListConverter
import com.seancoyle.database.util.VidUrlListConverter

@Database(
    entities =
    [
        LaunchSummaryEntity::class,
        LaunchEntity::class,
        LaunchRemoteKeyEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(
    AgencyListConverter::class,
    CountryListConverter::class,
    FamilyListConverter::class,
    InfoUrlListConverter::class,
    LaunchUpdateListConverter::class,
    LauncherStageListConverter::class,
    LocalDateTimeConverter::class,
    MissionPatchListConverter::class,
    ProgramListConverter::class,
    SpacecraftStageListConverter::class,
    VidUrlListConverter::class
)
abstract class Database : RoomDatabase() {

    abstract fun launchDao(): LaunchDao
    abstract fun launchRemoteKeyDao(): LaunchRemoteKeyDao

    companion object {
        const val DATABASE_NAME: String = "launches_db"
    }

}
