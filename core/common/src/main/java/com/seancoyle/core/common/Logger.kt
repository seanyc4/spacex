package com.seancoyle.core.common

import timber.log.Timber

fun printLogDebug(className: String?, message: String) {
    Timber.i("$className: $message")
}