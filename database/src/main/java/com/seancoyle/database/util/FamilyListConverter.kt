package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.FamilyEntity
import kotlinx.serialization.json.Json

class FamilyListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromFamilyList(value: List<FamilyEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toFamilyList(value: String?): List<FamilyEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<FamilyEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
