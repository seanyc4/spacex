package com.seancoyle.spacex.util

import com.seancoyle.spacex.util.Constants.DEBUG
import timber.log.Timber

var isUnitTest = false

fun printLogDebug(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Timber.d("$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}
