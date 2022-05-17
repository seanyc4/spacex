package com.seancoyle.spacex.framework.datasource.cache.model.launch

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.*
import com.seancoyle.spacex.framework.datasource.cache.typeconverters.LocalDateTimeTypeConverter
import java.time.LocalDateTime

@Entity(tableName = "launch")
data class LaunchEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Int,
    val launchDate: String,
    @TypeConverters(LocalDateTimeTypeConverter::class)
    val launchDateLocalDateTime: LocalDateTime,
    val isLaunchSuccess: Int?,
    @DrawableRes
    val launchSuccessIcon: Int,
    val launchYear: String,
    @Embedded
    val links: LinksEntity,
    val missionName: String,
    @Embedded
    val rocket: RocketEntity,
    @StringRes
    val daysToFromTitle: Int,
    val launchDaysDifference: String
)

data class LinksEntity(
    val missionImage: String,
    val articleLink: String,
    val videoLink: String,
    val wikipedia: String,
)

data class RocketEntity(
    val rocketNameAndType: String
)

