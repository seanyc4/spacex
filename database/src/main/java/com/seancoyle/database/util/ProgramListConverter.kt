package com.seancoyle.database.util

import androidx.room.TypeConverter
import com.seancoyle.database.entities.ProgramEntity
import kotlinx.serialization.json.Json

class ProgramListConverter {

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    @TypeConverter
    fun fromProgramList(value: List<ProgramEntity>?): String? {
        return value?.let {
            try {
                json.encodeToString(it)
            } catch (_: Exception) {
                null
            }
        }
    }

    @TypeConverter
    fun toProgramList(value: String?): List<ProgramEntity>? {
        return value?.let {
            try {
                json.decodeFromString<List<ProgramEntity>>(it)
            } catch (_: Exception) {
                null
            }
        }
    }
}
