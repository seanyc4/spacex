package com.seancoyle.core.common.analytics

interface AnalyticsLogger {
    fun init()

    fun logEvent(eventName: String, params: Map<String, String>? = null)
}
