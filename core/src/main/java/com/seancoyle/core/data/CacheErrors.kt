package com.seancoyle.core.data

object CacheErrors {
    const val CACHE_ERROR = "Database error: "
    const val CACHE_ERROR_TIMEOUT = "Database operation timed out. Please try again."
    const val CACHE_DATA_NULL = "Cache data is null"
    const val CONSTRAINT_VIOLATION = "A database constraint was violated."
    const val QUERY_SYNTAX_ERROR = "There was an error in the database query syntax."
    const val DATABASE_NOT_FOUND = "The requested database was not found."
    const val UNKNOWN_DATABASE_ERROR = "Unknown database error occurred."
}