package com.seancoyle.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.seancoyle.database.util.LocalDateTimeConverter
import com.seancoyle.feature.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.feature.launch.api.domain.model.LaunchStatus
import java.time.LocalDateTime

@Keep
@Entity(tableName = "launch", primaryKeys = ["id", "missionName"])
data class LaunchEntity(

    @ColumnInfo(name="id")
    val id: String,

    @ColumnInfo(name="launchDate")
    val launchDate: String,

    @TypeConverters(LocalDateTimeConverter::class)
    val launchDateLocalDateTime: LocalDateTime,

    @ColumnInfo(name="launchStatus")
    val launchStatus: LaunchStatus,

    @ColumnInfo(name="launchYear")
    val launchYear: String,

    @Embedded
    val links: LinksEntity,

    @ColumnInfo(name="missionName")
    val missionName: String,

    @Embedded
    val rocket: RocketEntity,

    @ColumnInfo(name="launchDateStatus")
    val launchDateStatus: LaunchDateStatus,

    @ColumnInfo(name="launchDaysDifference")
    val launchDays: String
)

@Keep
data class LinksEntity(
    @ColumnInfo(name="missionImage")
    val missionImage: String,

    @ColumnInfo(name="articleLink")
    val articleLink: String?,

    @ColumnInfo(name="videoLink")
    val webcastLink: String?,

    @ColumnInfo(name="wikipedia")
    val wikiLink: String?,
)

@Keep
data class RocketEntity(
    @ColumnInfo(name="rocketNameAndType")
    val rocketNameAndType: String
)