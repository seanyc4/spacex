package com.seancoyle.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "launch_remote_keys")
data class LaunchRemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val nextKey: Int?,
    val prevKey: Int?,
    val currentPage: Int,
    val createdAt: Long = System.currentTimeMillis(),
    val cachedQuery: String?,
    val cachedLaunchType: String?,
    val cachedLaunchStatus: String?
)
