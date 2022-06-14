package com.seancoyle.core.util

import com.seancoyle.core.util.Constants.DEBUG
import timber.log.Timber

var isUnitTest = false

fun printLogDebug(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Timber.i("$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}
