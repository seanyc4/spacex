package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.InfoUrlEntity
import kotlinx.serialization.json.Json

class InfoUrlListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromInfoUrlList(value: List<InfoUrlEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toInfoUrlList(value: String?): List<InfoUrlEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<InfoUrlEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
