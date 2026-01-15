package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.AgencyEntity
import kotlinx.serialization.json.Json

class AgencyListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromAgencyList(value: List<AgencyEntity>): String {
        return try {
            json.encodeToString(value)
        } catch (_: Exception) {
            ""
        }
    }

    @TypeConverter
    fun toAgencyList(value: String): List<AgencyEntity> {
        return try {
            json.decodeFromString<List<AgencyEntity>>(value)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
