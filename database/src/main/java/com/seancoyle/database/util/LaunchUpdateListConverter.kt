package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.LaunchUpdateEntity
import kotlinx.serialization.json.Json

class LaunchUpdateListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromLaunchUpdateList(value: List<LaunchUpdateEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toLaunchUpdateList(value: String?): List<LaunchUpdateEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<LaunchUpdateEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
