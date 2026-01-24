package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.SpacecraftStageEntity
import kotlinx.serialization.json.Json

class SpacecraftStageListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromSpacecraftStageList(value: List<SpacecraftStageEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toSpacecraftStageList(value: String?): List<SpacecraftStageEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<SpacecraftStageEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
