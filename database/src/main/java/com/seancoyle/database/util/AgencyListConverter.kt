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
    fun fromAgencyList(value: List<AgencyEntity?>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toAgencyList(value: String?): List<AgencyEntity?>? {
        return value?.let {
            try {
                json.decodeFromString<List<AgencyEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
