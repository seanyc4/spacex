package com.seancoyle.core.datastore.implementation.domain

import kotlinx.coroutines.flow.Flow

internal interface DataStorePreferences {

    suspend fun getString(key: String, defaultValue: String): String
    fun observeString(key: String, defaultValue: String): Flow<String>

    suspend fun getInt(key: String, defaultValue: Int): Int
    fun observeInt(key: String, defaultValue: Int): Flow<Int>

    suspend fun getLong(key: String, defaultValue: Long): Long
    fun observeLong(key: String, defaultValue: Long): Flow<Long>

    suspend fun getFloat(key: String, defaultValue: Float): Float
    fun observeFloat(key: String, defaultValue: Float): Flow<Float>

    suspend fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun observeBoolean(key: String, defaultValue: Boolean): Flow<Boolean>

    suspend fun contains(key: String): Boolean

    suspend fun delete(key: String)

    suspend fun saveString(
        key: String,
        value: String
    )

    suspend fun saveInt(
        key: String,
        value: Int
    )

    suspend fun saveLong(
        key: String,
        value: Long
    )

    suspend fun saveFloat(
        key: String,
        value: Float
    )

    suspend fun saveBoolean(
        key: String,
        value: Boolean
    )
}