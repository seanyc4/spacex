package com.seancoyle.core.util

interface Crashlytics {
    fun init(isEnabled: Boolean)

    fun setCustomKey(key: String, value: String)

    fun logException(throwable: Throwable)

    fun log(msg: String)
}