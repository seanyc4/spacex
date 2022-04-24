package com.seancoyle.movies.framework.datasource.cache.model.movielist

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MoviesTypeConverter {
    @TypeConverter
    fun stringToList(data: String?): List<ResultCache> {
        if (data.isNullOrEmpty()) {
            return emptyList()
        }

        val typeToken = object : TypeToken<List<ResultCache>>() {}.type

        return Gson().fromJson(data, typeToken)
    }

    @TypeConverter
    fun listToString(consolidatedFeedItems: List<ResultCache>): String {
        return Gson().toJson(consolidatedFeedItems)
    }
}