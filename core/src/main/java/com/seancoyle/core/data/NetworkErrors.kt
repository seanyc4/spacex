package com.seancoyle.core.data

object NetworkErrors {

    const val NETWORK_ERROR_TIMEOUT = "Request timed out. Please try again."
    const val NETWORK_CONNECTION_FAILED = "Failed to connect. Please check your internet connection."
    const val NETWORK_UNAUTHORIZED = "Unauthorized access." // 401
    const val NETWORK_FORBIDDEN = "Forbidden access." // 403
    const val NETWORK_NOT_FOUND = "The requested resource was not found." // 404
    const val NETWORK_INTERNAL_SERVER_ERROR = "Internal server error." // 500
    const val UNKNOWN_NETWORK_ERROR = "Unknown network error occurred."
    const val NETWORK_ERROR_UNKNOWN = "Network error: " // Use this prefix when you want to append error codes or additional details.
    const val NETWORK_DATA_NULL = "Network data is null."

}