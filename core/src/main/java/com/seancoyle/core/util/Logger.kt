package com.seancoyle.core.util

import timber.log.Timber

fun printLogDebug(className: String?, message: String) {
    Timber.i("$className: $message")
}