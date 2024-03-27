package com.seancoyle.core_database.api

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.TypeConverters
import com.seancoyle.core_database.api.typeconverters.LocalDateTimeTypeConverter
import com.seancoyle.launch.api.domain.model.LaunchDateStatus
import com.seancoyle.launch.api.domain.model.LaunchSuccessStatus
import java.time.LocalDateTime

@Keep
@Entity(tableName = "launch", primaryKeys = ["id", "missionName"])
data class LaunchEntity(

    @ColumnInfo(name="id")
    val id: String,

    @ColumnInfo(name="launchDate")
    val launchDate: String,

    @TypeConverters(LocalDateTimeTypeConverter::class)
    val launchDateLocalDateTime: LocalDateTime,

    @ColumnInfo(name="isLaunchSuccess")
    val isLaunchSuccess: Int?,

    @ColumnInfo(name="launchSuccessStatus")
    val launchSuccessStatus: LaunchSuccessStatus,

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
    val launchDaysDifference: String
)

@Keep
data class LinksEntity(
    @ColumnInfo(name="missionImage")
    val missionImage: String,

    @ColumnInfo(name="articleLink")
    val articleLink: String?,

    @ColumnInfo(name="videoLink")
    val videoLink: String?,

    @ColumnInfo(name="wikipedia")
    val wikipedia: String?,
)

@Keep
data class RocketEntity(
    @ColumnInfo(name="rocketNameAndType")
    val rocketNameAndType: String
)