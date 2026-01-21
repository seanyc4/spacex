package com.seancoyle.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.seancoyle.database.dao.PastDetailDao
import com.seancoyle.database.dao.PastLaunchDao
import com.seancoyle.database.dao.PastRemoteKeyDao
import com.seancoyle.database.dao.UpcomingDetailDao
import com.seancoyle.database.dao.UpcomingLaunchDao
import com.seancoyle.database.dao.UpcomingRemoteKeyDao
import com.seancoyle.database.entities.PastDetailEntity
import com.seancoyle.database.entities.PastLaunchEntity
import com.seancoyle.database.entities.PastRemoteKeyEntity
import com.seancoyle.database.entities.UpcomingDetailEntity
import com.seancoyle.database.entities.UpcomingLaunchEntity
import com.seancoyle.database.entities.UpcomingRemoteKeyEntity
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
        UpcomingDetailEntity::class,
        PastDetailEntity::class,
        UpcomingLaunchEntity::class,
        UpcomingRemoteKeyEntity::class,
        PastLaunchEntity::class,
        PastRemoteKeyEntity::class
    ],
    version = 4,
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
    abstract fun pastDetailDao(): PastDetailDao
    abstract fun upcomingDetailDao(): UpcomingDetailDao
    abstract fun upcomingLaunchDao(): UpcomingLaunchDao
    abstract fun upcomingRemoteKeyDao(): UpcomingRemoteKeyDao
    abstract fun pastLaunchDao(): PastLaunchDao
    abstract fun pastRemoteKeyDao(): PastRemoteKeyDao

    companion object {
        const val DATABASE_NAME: String = "launches_db"
    }

}
