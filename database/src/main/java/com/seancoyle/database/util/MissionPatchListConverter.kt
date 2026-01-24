package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.MissionPatchEntity
import kotlinx.serialization.json.Json

class MissionPatchListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromMissionPatchList(value: List<MissionPatchEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toMissionPatchList(value: String?): List<MissionPatchEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<MissionPatchEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
