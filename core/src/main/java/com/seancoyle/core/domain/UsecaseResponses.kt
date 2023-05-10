package com.seancoyle.core.domain

object UsecaseResponses {
    const val ERROR_UNKNOWN = "Unknown error"
    const val INVALID_STATE_EVENT = "Invalid state event"
    const val EVENT_NETWORK_EMPTY = "No data returned from network."
    const val EVENT_NETWORK_ERROR = "Error updating data from network.\n\nReason: Network error"
    const val EVENT_CACHE_INSERT_SUCCESS = "Successfully inserted data from network."
    const val EVENT_CACHE_INSERT_FAILED = "Failed to insert data from network."
    const val EVENT_CACHE_SUCCESS = "Successfully retrieved data from cache."
    const val EVENT_CACHE_NO_MATCHING_RESULTS = "There is not data that matches that query."
}











