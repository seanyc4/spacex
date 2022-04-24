package com.seancoyle.movies.util

import android.util.Log
import com.seancoyle.movies.util.Constants.DEBUG
import com.seancoyle.movies.util.Constants.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}
