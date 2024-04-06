package com.seancoyle.core.domain

enum class DataError : ErrorType {
    NETWORK_TIMEOUT,
    NETWORK_PAYLOAD_TOO_LARGE,
    NETWORK_CONNECTION_FAILED,
    NETWORK_UNAUTHORIZED,
    NETWORK_FORBIDDEN,
    NETWORK_NOT_FOUND,
    NETWORK_INTERNAL_SERVER_ERROR,
    NETWORK_UNKNOWN_ERROR,
    NETWORK_DATA_NULL,
    CACHE_ERROR,
    CACHE_ERROR_TIMEOUT,
    CACHE_ERROR_NO_RESULTS,
    CACHE_DATA_NULL,
    CACHE_CONSTRAINT_VIOLATION,
    CACHE_UNKNOWN_DATABASE_ERROR,
    CACHE_INSERT_FAILED;
}