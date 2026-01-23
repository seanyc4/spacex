package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.LauncherStageEntity
import kotlinx.serialization.json.Json

class LauncherStageListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromLauncherStageList(value: List<LauncherStageEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toLauncherStageList(value: String?): List<LauncherStageEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<LauncherStageEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
