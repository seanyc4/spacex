package com.seancoyle.core.util

import timber.log.Timber

var isUnitTest = false
const val TAG = "SPACEXAPP: " // Tag for logs
const val DEBUG = true // enable logging

fun printLogDebug(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Timber.i("$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}
