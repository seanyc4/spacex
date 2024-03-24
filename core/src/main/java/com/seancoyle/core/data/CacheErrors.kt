package com.seancoyle.core.data

object CacheErrors {
    const val CACHE_ERROR = "Database error: "
    const val CACHE_ERROR_TIMEOUT = "Database operation timed out. Please try again."
    const val CACHE_ERROR_NO_RESULTS = "No matching results. Please try again."
    const val CACHE_DATA_NULL = "Cache data is null"
    const val CONSTRAINT_VIOLATION = "A database constraint was violated."
    const val UNKNOWN_DATABASE_ERROR = "Unknown database error occurred."
    const val CACHE_INSERT_FAILED = "Failed to insert data into cache."
}