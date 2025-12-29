package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.CountryEntity
import kotlinx.serialization.json.Json

class CountryListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
        explicitNulls = false
        coerceInputValues = true
    }

    @TypeConverter
    fun fromCountryList(value: List<CountryEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toCountryList(value: String?): List<CountryEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<CountryEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}

