package com.seancoyle.movies.framework.datasource.cache.model.moviedetail

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieCastTypeConverter {
    @TypeConverter
    fun stringToList(data: String?): List<CastCacheEntity> {
        if (data.isNullOrEmpty()) {
            return emptyList()
        }

        val typeToken = object : TypeToken<List<CastCacheEntity>>() {}.type

        return Gson().fromJson(data, typeToken)
    }

    @TypeConverter
    fun listToString(consolidatedFeedItems: List<CastCacheEntity>): String {
        return Gson().toJson(consolidatedFeedItems)
    }
}