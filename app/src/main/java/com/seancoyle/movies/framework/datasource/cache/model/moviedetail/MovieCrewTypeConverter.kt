package com.seancoyle.movies.framework.datasource.cache.model.moviedetail

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieCrewTypeConverter {
    @TypeConverter
    fun stringToList(data: String?): List<CrewCacheEntity> {
        if (data.isNullOrEmpty()) {
            return emptyList()
        }

        val typeToken = object : TypeToken<List<CrewCacheEntity>>() {}.type

        return Gson().fromJson(data, typeToken)
    }

    @TypeConverter
    fun listToString(consolidatedFeedItems: List<CrewCacheEntity>): String {
        return Gson().toJson(consolidatedFeedItems)
    }
}