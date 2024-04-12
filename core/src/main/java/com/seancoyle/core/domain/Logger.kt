package com.seancoyle.core.domain

import timber.log.Timber

fun printLogDebug(className: String?, message: String) {
    Timber.i("$className: $message")
}