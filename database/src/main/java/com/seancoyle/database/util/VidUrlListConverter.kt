package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.VidUrlEntity
import kotlinx.serialization.json.Json

class VidUrlListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromVidUrlList(value: List<VidUrlEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toVidUrlList(value: String?): List<VidUrlEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<VidUrlEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
