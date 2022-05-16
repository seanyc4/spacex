package com.seancoyle.spacex.framework.datasource.cache.model.launch

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.room.*
import com.seancoyle.spacex.framework.datasource.cache.typeconverters.LocalDateTimeTypeConverter
import java.time.LocalDateTime

@Entity(tableName = "launch")
data class LaunchCacheEntity(

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
    val links: LinksCache,
    val missionName: String,
    @Embedded
    val rocket: RocketCache,
    @StringRes
    val daysToFromTitle: Int,
    val launchDaysDifference: String
)

data class LinksCache(
    val missionImage: String,
    val articleLink: String,
    val videoLink: String,
    val wikipedia: String,
)

data class RocketCache(
    val rocketNameAndType: String
)

