package com.seancoyle.database.entities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity

@Keep
@Entity(tableName = "upcoming_launches", primaryKeys = ["id"])
data class UpcomingLaunchEntity(

    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "mission_name")
    val missionName: String,

    @ColumnInfo(name = "net")
    val net: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @Embedded(prefix = "status")
    val status: LaunchStatusEntity
)
